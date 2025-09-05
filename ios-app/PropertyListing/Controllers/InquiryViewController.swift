import UIKit

class InquiryViewController: UIViewController {
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var propertyInfoView: UIView!
    @IBOutlet weak var propertyTitleLabel: UILabel!
    @IBOutlet weak var propertyLocationLabel: UILabel!
    @IBOutlet weak var inquiryTextView: UITextView!
    @IBOutlet weak var termsTextView: UITextView!
    @IBOutlet weak var termsCheckbox: UIButton!
    @IBOutlet weak var submitButton: UIButton!
    @IBOutlet weak var loadingIndicator: UIActivityIndicatorView!
    
    var property: Property!
    private var isTermsAccepted = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        configureWithProperty()
        setupKeyboardHandling()
    }
    
    private func setupUI() {
        title = "Create Inquiry"
        
        // Navigation bar
        navigationItem.leftBarButtonItem = UIBarButtonItem(
            barButtonSystemItem: .cancel,
            target: self,
            action: #selector(cancelTapped)
        )
        
        // Property info view
        propertyInfoView.backgroundColor = .systemGray6
        propertyInfoView.roundCorners(radius: 12)
        
        propertyTitleLabel.font = UIFont.systemFont(ofSize: 18, weight: .semibold)
        propertyTitleLabel.textColor = .primaryColor
        propertyTitleLabel.numberOfLines = 0
        
        propertyLocationLabel.font = UIFont.systemFont(ofSize: 14, weight: .medium)
        propertyLocationLabel.textColor = .secondaryLabel
        
        // Inquiry text view
        inquiryTextView.font = UIFont.systemFont(ofSize: 16)
        inquiryTextView.layer.borderColor = UIColor.systemGray4.cgColor
        inquiryTextView.layer.borderWidth = 1
        inquiryTextView.layer.cornerRadius = 12
        inquiryTextView.textContainerInset = UIEdgeInsets(top: 12, left: 12, bottom: 12, right: 12)
        inquiryTextView.text = "I am interested in this property. Please contact me with more details."
        
        // Terms text view
        termsTextView.font = UIFont.systemFont(ofSize: 14)
        termsTextView.textColor = .secondaryLabel
        termsTextView.backgroundColor = .systemGray6
        termsTextView.layer.cornerRadius = 8
        termsTextView.isEditable = false
        termsTextView.text = """
        Terms & Conditions:
        
        • Inquiry response period is 3 months from the date of inquiry
        • Seller is responsible for verifying that inquiries are not fraudulent
        • We only provide inquiry connections between buyers and sellers
        • Seller must self-update property details for accuracy
        • Your inquiry will be sent to the property seller
        • Please verify all property details independently
        """
        
        // Terms checkbox
        termsCheckbox.setImage(UIImage(systemName: "square"), for: .normal)
        termsCheckbox.setImage(UIImage(systemName: "checkmark.square.fill"), for: .selected)
        termsCheckbox.tintColor = .primaryColor
        
        // Submit button
        submitButton.setTitle("Submit Inquiry", for: .normal)
        submitButton.setTitleColor(.white, for: .normal)
        submitButton.backgroundColor = .secondaryColor
        submitButton.titleLabel?.font = UIFont.systemFont(ofSize: 18, weight: .semibold)
        submitButton.roundCorners(radius: 12)
        submitButton.addShadow()
        submitButton.isEnabled = false
        submitButton.alpha = 0.6
        
        // Loading indicator
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.color = .primaryColor
    }
    
    private func configureWithProperty() {
        guard let property = property else { return }
        
        propertyTitleLabel.text = property.title
        propertyLocationLabel.text = "\(property.city), \(property.state)"
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
    
    @objc private func cancelTapped() {
        dismiss(animated: true)
    }
    
    @IBAction func termsCheckboxTapped(_ sender: UIButton) {
        isTermsAccepted.toggle()
        sender.isSelected = isTermsAccepted
        
        submitButton.isEnabled = isTermsAccepted
        submitButton.alpha = isTermsAccepted ? 1.0 : 0.6
    }
    
    @IBAction func submitButtonTapped(_ sender: UIButton) {
        guard isTermsAccepted else {
            showAlert(title: "Error", message: "Please accept the terms and conditions")
            return
        }
        
        guard let propertyId = property.id else {
            showAlert(title: "Error", message: "Invalid property information")
            return
        }
        
        let inquiryDescription = inquiryTextView.text.trimmingCharacters(in: .whitespacesAndNewlines)
        let finalDescription = inquiryDescription.isEmpty ? "I am interested in this property. Please contact me." : inquiryDescription
        
        let inquiryRequest = InquiryRequest(
            propertyId: propertyId,
            inquiryDescription: finalDescription,
            termsAccepted: true
        )
        
        submitInquiry(request: inquiryRequest)
    }
    
    private func submitInquiry(request: InquiryRequest) {
        guard let currentUser = UserManager.shared.currentUser else {
            showAlert(title: "Error", message: "Please login to submit inquiry")
            return
        }
        
        loadingIndicator.startAnimating()
        submitButton.isEnabled = false
        
        // Use mobile number as token for now (matching Android implementation)
        let token = currentUser.mobileNumber
        
        APIService.shared.createInquiry(token: token, request: request) { [weak self] result in
            DispatchQueue.main.async {
                self?.loadingIndicator.stopAnimating()
                self?.submitButton.isEnabled = true
                
                switch result {
                case .success(let response):
                    if response.success {
                        self?.showAlert(title: "Success", message: "Inquiry submitted successfully!") {
                            self?.dismiss(animated: true)
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
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
}
