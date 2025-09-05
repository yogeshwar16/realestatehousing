import UIKit

class ProfileViewController: UIViewController {
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var profileHeaderView: UIView!
    @IBOutlet weak var profileImageView: UIImageView!
    @IBOutlet weak var userNameLabel: UILabel!
    @IBOutlet weak var userTypeLabel: UILabel!
    
    @IBOutlet weak var fullNameTextField: UITextField!
    @IBOutlet weak var mobileNumberTextField: UITextField!
    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var aadhaarTextField: UITextField!
    @IBOutlet weak var panTextField: UITextField!
    @IBOutlet weak var addressTextView: UITextView!
    
    @IBOutlet weak var updateButton: UIButton!
    @IBOutlet weak var termsButton: UIButton!
    @IBOutlet weak var logoutButton: UIButton!
    @IBOutlet weak var loadingIndicator: UIActivityIndicatorView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        loadUserData()
        setupKeyboardHandling()
    }
    
    private func setupUI() {
        title = "Profile"
        navigationController?.navigationBar.prefersLargeTitles = true
        
        // Profile header
        profileHeaderView.backgroundColor = .primaryColor
        profileHeaderView.roundCorners(radius: 16)
        
        profileImageView.backgroundColor = .white
        profileImageView.layer.cornerRadius = 40
        profileImageView.clipsToBounds = true
        profileImageView.image = UIImage(systemName: "person.fill")
        profileImageView.tintColor = .primaryColor
        profileImageView.contentMode = .scaleAspectFit
        
        userNameLabel.font = UIFont.systemFont(ofSize: 20, weight: .bold)
        userNameLabel.textColor = .white
        
        userTypeLabel.font = UIFont.systemFont(ofSize: 16, weight: .medium)
        userTypeLabel.textColor = UIColor.white.withAlphaComponent(0.8)
        
        // Text fields
        setupTextField(fullNameTextField, placeholder: "Full Name", icon: "person.fill")
        setupTextField(mobileNumberTextField, placeholder: "Mobile Number", icon: "phone.fill")
        setupTextField(emailTextField, placeholder: "Email Address", icon: "envelope.fill")
        setupTextField(aadhaarTextField, placeholder: "Aadhaar Number", icon: "creditcard.fill")
        setupTextField(panTextField, placeholder: "PAN Card Number", icon: "doc.text.fill")
        
        // Mobile number is read-only
        mobileNumberTextField.isEnabled = false
        mobileNumberTextField.backgroundColor = .systemGray6
        
        emailTextField.keyboardType = .emailAddress
        aadhaarTextField.keyboardType = .numberPad
        panTextField.autocapitalizationType = .allCharacters
        
        // Address text view
        addressTextView.font = UIFont.systemFont(ofSize: 16)
        addressTextView.layer.borderColor = UIColor.systemGray4.cgColor
        addressTextView.layer.borderWidth = 1
        addressTextView.layer.cornerRadius = 12
        addressTextView.textContainerInset = UIEdgeInsets(top: 12, left: 12, bottom: 12, right: 12)
        
        // Update button
        updateButton.setTitle("Update Profile", for: .normal)
        updateButton.backgroundColor = .primaryColor
        updateButton.setTitleColor(.white, for: .normal)
        updateButton.titleLabel?.font = UIFont.systemFont(ofSize: 18, weight: .semibold)
        updateButton.roundCorners(radius: 12)
        updateButton.addShadow()
        
        // Terms button
        termsButton.setTitle("Terms & Conditions", for: .normal)
        termsButton.setTitleColor(.primaryColor, for: .normal)
        termsButton.titleLabel?.font = UIFont.systemFont(ofSize: 16, weight: .medium)
        termsButton.backgroundColor = .systemGray6
        termsButton.roundCorners(radius: 8)
        
        // Logout button
        logoutButton.setTitle("Logout", for: .normal)
        logoutButton.setTitleColor(.white, for: .normal)
        logoutButton.backgroundColor = .systemRed
        logoutButton.titleLabel?.font = UIFont.systemFont(ofSize: 18, weight: .semibold)
        logoutButton.roundCorners(radius: 12)
        
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
    
    private func loadUserData() {
        guard let user = UserManager.shared.currentUser else { return }
        
        userNameLabel.text = user.fullName
        userTypeLabel.text = user.userType.displayName
        
        fullNameTextField.text = user.fullName
        mobileNumberTextField.text = user.mobileNumber
        emailTextField.text = user.email
        aadhaarTextField.text = user.aadhaarNumber
        panTextField.text = user.panCard
        addressTextView.text = user.address
    }
    
    @IBAction func updateButtonTapped(_ sender: UIButton) {
        guard validateInputs() else { return }
        
        guard let currentUser = UserManager.shared.currentUser else {
            showAlert(title: "Error", message: "User session expired. Please login again.")
            return
        }
        
        let updateRequest = UserUpdateRequest(
            fullName: fullNameTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines),
            email: emailTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines),
            aadhaarNumber: aadhaarTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines),
            panCard: panTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines),
            address: addressTextView.text.isEmpty ? nil : addressTextView.text.trimmingCharacters(in: .whitespacesAndNewlines)
        )
        
        updateProfile(request: updateRequest, mobileNumber: currentUser.mobileNumber)
    }
    
    @IBAction func termsButtonTapped(_ sender: UIButton) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let termsVC = storyboard.instantiateViewController(withIdentifier: "TermsConditionsViewController")
        let navController = UINavigationController(rootViewController: termsVC)
        present(navController, animated: true)
    }
    
    @IBAction func logoutButtonTapped(_ sender: UIButton) {
        showConfirmationAlert(
            title: "Logout",
            message: "Are you sure you want to logout?",
            confirmTitle: "Logout"
        ) {
            self.performLogout()
        }
    }
    
    private func validateInputs() -> Bool {
        guard let fullName = fullNameTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines),
              !fullName.isEmpty else {
            showAlert(title: "Error", message: "Please enter your full name")
            return false
        }
        
        guard let email = emailTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines),
              email.isValidEmail else {
            showAlert(title: "Error", message: "Please enter a valid email address")
            return false
        }
        
        if let aadhaar = aadhaarTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines),
           !aadhaar.isEmpty && !aadhaar.isValidAadhaar {
            showAlert(title: "Error", message: "Please enter a valid Aadhaar number")
            return false
        }
        
        if let pan = panTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines),
           !pan.isEmpty && !pan.isValidPAN {
            showAlert(title: "Error", message: "Please enter a valid PAN card number")
            return false
        }
        
        return true
    }
    
    private func updateProfile(request: UserUpdateRequest, mobileNumber: String) {
        loadingIndicator.startAnimating()
        updateButton.isEnabled = false
        
        APIService.shared.updateUser(mobileNumber: mobileNumber, request: request) { [weak self] result in
            DispatchQueue.main.async {
                self?.loadingIndicator.stopAnimating()
                self?.updateButton.isEnabled = true
                
                switch result {
                case .success(let response):
                    if response.success, let updatedUser = response.data {
                        UserManager.shared.updateUser(updatedUser)
                        self?.loadUserData()
                        self?.showAlert(title: "Success", message: "Profile updated successfully!")
                    } else {
                        self?.showAlert(title: "Error", message: response.message)
                    }
                case .failure(let error):
                    self?.showAlert(title: "Error", message: error.localizedDescription)
                }
            }
        }
    }
    
    private func performLogout() {
        UserManager.shared.logout()
        
        // Navigate to splash screen
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let splashVC = storyboard.instantiateViewController(withIdentifier: "SplashViewController")
        let navController = UINavigationController(rootViewController: splashVC)
        navController.isNavigationBarHidden = true
        
        if let sceneDelegate = UIApplication.shared.connectedScenes.first?.delegate as? SceneDelegate {
            sceneDelegate.window?.rootViewController = navController
            sceneDelegate.window?.makeKeyAndVisible()
        }
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
}
