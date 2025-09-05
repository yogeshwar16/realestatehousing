import UIKit

class LoginViewController: UIViewController {
    
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var subtitleLabel: UILabel!
    @IBOutlet weak var mobileNumberTextField: UITextField!
    @IBOutlet weak var otpContainerView: UIView!
    @IBOutlet weak var otpTextField: UITextField!
    @IBOutlet weak var sendOTPButton: UIButton!
    @IBOutlet weak var loginButton: UIButton!
    @IBOutlet weak var resendButton: UIButton!
    @IBOutlet weak var timerLabel: UILabel!
    @IBOutlet weak var loadingIndicator: UIActivityIndicatorView!
    
    private var otpTimer: Timer?
    private var remainingTime = 60
    private var isOTPSent = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        hideKeyboardWhenTappedAround()
    }
    
    private func setupUI() {
        view.backgroundColor = .backgroundLight
        
        // Navigation
        navigationItem.title = "Login"
        navigationController?.navigationBar.prefersLargeTitles = true
        
        // Title and subtitle
        titleLabel.text = "Welcome Back"
        titleLabel.font = UIFont.systemFont(ofSize: 28, weight: .bold)
        titleLabel.textColor = .primaryColor
        
        subtitleLabel.text = "Enter your mobile number to continue"
        subtitleLabel.font = UIFont.systemFont(ofSize: 16, weight: .medium)
        subtitleLabel.textColor = .systemGray
        
        // Mobile number text field
        setupTextField(mobileNumberTextField, placeholder: "Mobile Number", icon: "phone.fill")
        mobileNumberTextField.keyboardType = .phonePad
        
        // OTP text field
        setupTextField(otpTextField, placeholder: "Enter OTP", icon: "lock.fill")
        otpTextField.keyboardType = .numberPad
        otpTextField.isSecureTextEntry = true
        
        // OTP container initially hidden
        otpContainerView.isHidden = true
        otpContainerView.alpha = 0
        
        // Send OTP button
        sendOTPButton.setTitle("Send OTP", for: .normal)
        sendOTPButton.backgroundColor = .primaryColor
        sendOTPButton.setTitleColor(.white, for: .normal)
        sendOTPButton.titleLabel?.font = UIFont.systemFont(ofSize: 18, weight: .semibold)
        sendOTPButton.roundCorners(radius: 12)
        sendOTPButton.addShadow()
        
        // Login button
        loginButton.setTitle("Login", for: .normal)
        loginButton.backgroundColor = .successColor
        loginButton.setTitleColor(.white, for: .normal)
        loginButton.titleLabel?.font = UIFont.systemFont(ofSize: 18, weight: .semibold)
        loginButton.roundCorners(radius: 12)
        loginButton.addShadow()
        loginButton.isEnabled = false
        loginButton.alpha = 0.6
        
        // Resend button
        resendButton.setTitle("Resend OTP", for: .normal)
        resendButton.setTitleColor(.primaryColor, for: .normal)
        resendButton.titleLabel?.font = UIFont.systemFont(ofSize: 16, weight: .medium)
        resendButton.isEnabled = false
        
        // Timer label
        timerLabel.font = UIFont.systemFont(ofSize: 14, weight: .medium)
        timerLabel.textColor = .systemGray
        timerLabel.isHidden = true
        
        // Loading indicator
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.color = .primaryColor
    }
    
    private func setupTextField(_ textField: UITextField, placeholder: String, icon: String) {
        textField.placeholder = placeholder
        textField.font = UIFont.systemFont(ofSize: 16)
        textField.borderStyle = .none
        textField.backgroundColor = .white
        textField.layer.cornerRadius = 12
        textField.layer.borderWidth = 1
        textField.layer.borderColor = UIColor.systemGray4.cgColor
        textField.addPadding(left: 50, right: 16)
        
        // Add icon
        let iconImageView = UIImageView(image: UIImage(systemName: icon))
        iconImageView.tintColor = .systemGray2
        iconImageView.contentMode = .scaleAspectFit
        iconImageView.frame = CGRect(x: 15, y: 0, width: 20, height: 20)
        
        let iconContainerView = UIView(frame: CGRect(x: 0, y: 0, width: 50, height: textField.frame.height))
        iconContainerView.addSubview(iconImageView)
        iconImageView.center = iconContainerView.center
        
        textField.leftView = iconContainerView
        textField.leftViewMode = .always
    }
    
    @IBAction func sendOTPTapped(_ sender: UIButton) {
        guard let mobileNumber = mobileNumberTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines),
              mobileNumber.isValidMobileNumber else {
            showAlert(title: "Error", message: "Please enter a valid mobile number")
            return
        }
        
        let otpRequest = OTPRequest(mobileNumber: mobileNumber)
        
        loadingIndicator.startAnimating()
        sendOTPButton.isEnabled = false
        
        APIService.shared.sendOTP(request: otpRequest) { [weak self] result in
            DispatchQueue.main.async {
                self?.loadingIndicator.stopAnimating()
                
                switch result {
                case .success(let response):
                    if response.success {
                        self?.showOTPSection()
                        self?.startOTPTimer()
                        self?.showAlert(title: "OTP Sent", message: "OTP has been sent to your mobile number")
                    } else {
                        self?.sendOTPButton.isEnabled = true
                        self?.showAlert(title: "Error", message: response.message)
                    }
                case .failure(let error):
                    self?.sendOTPButton.isEnabled = true
                    self?.showAlert(title: "Error", message: error.localizedDescription)
                }
            }
        }
    }
    
    @IBAction func loginTapped(_ sender: UIButton) {
        guard let mobileNumber = mobileNumberTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines),
              let otp = otpTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines),
              !otp.isEmpty else {
            showAlert(title: "Error", message: "Please enter the OTP")
            return
        }
        
        let loginRequest = LoginRequest(mobileNumber: mobileNumber, otp: otp)
        
        loadingIndicator.startAnimating()
        loginButton.isEnabled = false
        
        APIService.shared.login(request: loginRequest) { [weak self] result in
            DispatchQueue.main.async {
                self?.loadingIndicator.stopAnimating()
                self?.loginButton.isEnabled = true
                
                switch result {
                case .success(let response):
                    if response.success, let user = response.data {
                        UserManager.shared.login(user: user)
                        self?.navigateToMainApp()
                    } else {
                        self?.showAlert(title: "Error", message: response.message)
                    }
                case .failure(let error):
                    self?.showAlert(title: "Error", message: error.localizedDescription)
                }
            }
        }
    }
    
    @IBAction func resendOTPTapped(_ sender: UIButton) {
        sendOTPTapped(sendOTPButton)
    }
    
    private func showOTPSection() {
        isOTPSent = true
        
        UIView.animate(withDuration: 0.5, animations: {
            self.otpContainerView.isHidden = false
            self.otpContainerView.alpha = 1
            self.loginButton.isEnabled = true
            self.loginButton.alpha = 1
        })
        
        subtitleLabel.text = "Enter the OTP sent to your mobile number"
        sendOTPButton.setTitle("OTP Sent", for: .normal)
        sendOTPButton.backgroundColor = .systemGray4
        sendOTPButton.isEnabled = false
    }
    
    private func startOTPTimer() {
        remainingTime = 60
        timerLabel.isHidden = false
        resendButton.isEnabled = false
        
        otpTimer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: true) { [weak self] timer in
            guard let self = self else {
                timer.invalidate()
                return
            }
            
            self.remainingTime -= 1
            self.timerLabel.text = "Resend OTP in \(self.remainingTime)s"
            
            if self.remainingTime <= 0 {
                timer.invalidate()
                self.timerLabel.isHidden = true
                self.resendButton.isEnabled = true
                self.sendOTPButton.setTitle("Send OTP", for: .normal)
                self.sendOTPButton.backgroundColor = .primaryColor
                self.sendOTPButton.isEnabled = true
            }
        }
    }
    
    private func navigateToMainApp() {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let mainTabBarController = storyboard.instantiateViewController(withIdentifier: "MainTabBarController")
        
        // Set as root view controller
        if let sceneDelegate = UIApplication.shared.connectedScenes.first?.delegate as? SceneDelegate {
            sceneDelegate.window?.rootViewController = mainTabBarController
            sceneDelegate.window?.makeKeyAndVisible()
        }
    }
    
    deinit {
        otpTimer?.invalidate()
    }
}
