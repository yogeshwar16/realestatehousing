import UIKit

class SignupViewController: UIViewController {
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var subtitleLabel: UILabel!
    
    @IBOutlet weak var fullNameTextField: UITextField!
    @IBOutlet weak var mobileNumberTextField: UITextField!
    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var aadhaarTextField: UITextField!
    @IBOutlet weak var panTextField: UITextField!
    @IBOutlet weak var addressTextView: UITextView!
    
    @IBOutlet weak var userTypeSegmentedControl: UISegmentedControl!
    @IBOutlet weak var termsCheckbox: UIButton!
    @IBOutlet weak var signupButton: UIButton!
    @IBOutlet weak var loginButton: UIButton!
    @IBOutlet weak var loadingIndicator: UIActivityIndicatorView!
    
    private var isTermsAccepted = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        setupKeyboardHandling()
    }
    
    private func setupUI() {
        view.backgroundColor = .backgroundLight
        
        // Title and subtitle
        titleLabel.text = "Create Account"
        titleLabel.font = UIFont.systemFont(ofSize: 28, weight: .bold)
        titleLabel.textColor = .primaryColor
        
        subtitleLabel.text = "Join us to find your perfect property"
        subtitleLabel.font = UIFont.systemFont(ofSize: 16, weight: .medium)
        subtitleLabel.textColor = .systemGray
        
        // Text fields setup
        setupTextField(fullNameTextField, placeholder: "Full Name", icon: "person.fill")
        setupTextField(mobileNumberTextField, placeholder: "Mobile Number", icon: "phone.fill")
        setupTextField(emailTextField, placeholder: "Email Address", icon: "envelope.fill")
        setupTextField(aadhaarTextField, placeholder: "Aadhaar Number", icon: "creditcard.fill")
        setupTextField(panTextField, placeholder: "PAN Card Number", icon: "doc.text.fill")
        
        mobileNumberTextField.keyboardType = .phonePad
        emailTextField.keyboardType = .emailAddress
        aadhaarTextField.keyboardType = .numberPad
        panTextField.autocapitalizationType = .allCharacters
        
        // Address text view
        addressTextView.layer.borderColor = UIColor.systemGray4.cgColor
        addressTextView.layer.borderWidth = 1
        addressTextView.layer.cornerRadius = 12
        addressTextView.font = UIFont.systemFont(ofSize: 16)
        addressTextView.textContainerInset = UIEdgeInsets(top: 12, left: 12, bottom: 12, right: 12)
        
        // User type segmented control
        userTypeSegmentedControl.setTitle("Customer", forSegmentAt: 0)
        userTypeSegmentedControl.setTitle("Seller", forSegmentAt: 1)
        userTypeSegmentedControl.selectedSegmentIndex = 0
        userTypeSegmentedControl.backgroundColor = .systemGray6
        userTypeSegmentedControl.selectedSegmentTintColor = .primaryColor
        userTypeSegmentedControl.setTitleTextAttributes([.foregroundColor: UIColor.white], for: .selected)
        
        // Terms checkbox
        termsCheckbox.setImage(UIImage(systemName: "square"), for: .normal)
        termsCheckbox.setImage(UIImage(systemName: "checkmark.square.fill"), for: .selected)
        termsCheckbox.tintColor = .primaryColor
        
        // Signup button
        signupButton.setTitle("Create Account", for: .normal)
        signupButton.backgroundColor = .primaryColor
        signupButton.setTitleColor(.white, for: .normal)
        signupButton.titleLabel?.font = UIFont.systemFont(ofSize: 18, weight: .semibold)
        signupButton.roundCorners(radius: 12)
        signupButton.addShadow()
        
        // Login button
        loginButton.setTitle("Already have an account? Login", for: .normal)
        loginButton.setTitleColor(.primaryColor, for: .normal)
        loginButton.titleLabel?.font = UIFont.systemFont(ofSize: 16, weight: .medium)
        
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
    
    private func setupKeyboardHandling() {
        hideKeyboardWhenTappedAround()
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(keyboardWillShow),
            name: UIResponder.keyboardWillShowNotification,
            object: nil
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(keyboardWillHide),
            name: UIResponder.keyboardWillHideNotification,
            object: nil
        )
    }
    
    @objc private func keyboardWillShow(notification: NSNotification) {
        guard let keyboardSize = (notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue else { return }
        
        let contentInsets = UIEdgeInsets(top: 0, left: 0, bottom: keyboardSize.height, right: 0)
        scrollView.contentInset = contentInsets
        scrollView.scrollIndicatorInsets = contentInsets
    }
    
    @objc private func keyboardWillHide(notification: NSNotification) {
        scrollView.contentInset = .zero
        scrollView.scrollIndicatorInsets = .zero
    }
    
    @IBAction func termsCheckboxTapped(_ sender: UIButton) {
        isTermsAccepted.toggle()
        sender.isSelected = isTermsAccepted
    }
    
    @IBAction func signupButtonTapped(_ sender: UIButton) {
        guard validateInputs() else { return }
        
        let userType: UserType = userTypeSegmentedControl.selectedSegmentIndex == 0 ? .customer : .seller
        
        let signupRequest = SignupRequest(
            fullName: fullNameTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines),
            mobileNumber: mobileNumberTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines),
            email: emailTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines),
            aadhaarNumber: aadhaarTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines),
            panCard: panTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines),
            userType: userType,
            address: addressTextView.text.isEmpty ? nil : addressTextView.text.trimmingCharacters(in: .whitespacesAndNewlines)
        )
        
        performSignup(request: signupRequest)
    }
    
    @IBAction func loginButtonTapped(_ sender: UIButton) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let loginVC = storyboard.instantiateViewController(withIdentifier: "LoginViewController")
        navigationController?.pushViewController(loginVC, animated: true)
    }
    
    private func validateInputs() -> Bool {
        guard let fullName = fullNameTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines),
              !fullName.isEmpty else {
            showAlert(title: "Error", message: "Please enter your full name")
            return false
        }
        
        guard let mobileNumber = mobileNumberTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines),
              mobileNumber.isValidMobileNumber else {
            showAlert(title: "Error", message: "Please enter a valid mobile number")
            return false
        }
        
        guard let email = emailTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines),
              email.isValidEmail else {
            showAlert(title: "Error", message: "Please enter a valid email address")
            return false
        }
        
        guard let aadhaar = aadhaarTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines),
              aadhaar.isValidAadhaar else {
            showAlert(title: "Error", message: "Please enter a valid Aadhaar number")
            return false
        }
        
        guard let pan = panTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines),
              pan.isValidPAN else {
            showAlert(title: "Error", message: "Please enter a valid PAN card number")
            return false
        }
        
        guard isTermsAccepted else {
            showAlert(title: "Error", message: "Please accept the terms and conditions")
            return false
        }
        
        return true
    }
    
    private func performSignup(request: SignupRequest) {
        loadingIndicator.startAnimating()
        signupButton.isEnabled = false
        
        APIService.shared.signup(request: request) { [weak self] result in
            DispatchQueue.main.async {
                self?.loadingIndicator.stopAnimating()
                self?.signupButton.isEnabled = true
                
                switch result {
                case .success(let response):
                    if response.success {
                        self?.showAlert(title: "Success", message: "Account created successfully! Please login to continue.") {
                            self?.navigateToLogin()
                        }
                    } else {
                        self?.showAlert(title: "Error", message: response.message)
                    }
                case .failure(let error):
                    self?.showAlert(title: "Error", message: error.localizedDescription)
                }
            }
        }
    }
    
    private func navigateToLogin() {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let loginVC = storyboard.instantiateViewController(withIdentifier: "LoginViewController")
        navigationController?.pushViewController(loginVC, animated: true)
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
}
