import UIKit

class TermsConditionsViewController: UIViewController {
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var lastUpdatedLabel: UILabel!
    @IBOutlet weak var termsTextView: UITextView!
    @IBOutlet weak var acceptButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        loadTermsContent()
    }
    
    private func setupUI() {
        title = "Terms & Conditions"
        
        // Navigation bar
        navigationItem.leftBarButtonItem = UIBarButtonItem(
            barButtonSystemItem: .cancel,
            target: self,
            action: #selector(cancelTapped)
        )
        
        // Title label
        titleLabel.text = "Property Listing App - Terms & Conditions"
        titleLabel.font = UIFont.systemFont(ofSize: 20, weight: .bold)
        titleLabel.textColor = .primaryColor
        titleLabel.textAlignment = .center
        titleLabel.numberOfLines = 0
        
        // Last updated label
        lastUpdatedLabel.text = "Last Updated: January 2024"
        lastUpdatedLabel.font = UIFont.systemFont(ofSize: 12, weight: .medium)
        lastUpdatedLabel.textColor = .secondaryLabel
        lastUpdatedLabel.textAlignment = .center
        
        // Terms text view
        termsTextView.font = UIFont.systemFont(ofSize: 14)
        termsTextView.textColor = .label
        termsTextView.backgroundColor = .systemBackground
        termsTextView.isEditable = false
        termsTextView.showsVerticalScrollIndicator = true
        
        // Accept button
        acceptButton.setTitle("I Accept Terms & Conditions", for: .normal)
        acceptButton.setTitleColor(.white, for: .normal)
        acceptButton.backgroundColor = .primaryColor
        acceptButton.titleLabel?.font = UIFont.systemFont(ofSize: 18, weight: .semibold)
        acceptButton.roundCorners(radius: 12)
        acceptButton.addShadow()
    }
    
    private func loadTermsContent() {
        let termsText = """
        1. Acceptance of Terms

        By using this Property Listing App, you agree to be bound by these Terms and Conditions. If you do not agree to these terms, please do not use our services.

        2. User Registration

        • Users must provide accurate and complete information during registration
        • Mobile number verification is mandatory
        • Users are responsible for maintaining account security
        • One account per mobile number is allowed

        3. Property Listings

        • Sellers must provide accurate property information
        • Property images and documents must be authentic
        • Misleading or fraudulent listings are prohibited
        • We reserve the right to remove inappropriate listings

        4. Inquiry System

        • Inquiries are valid for 3 months from creation date
        • Customers can inquire about multiple properties
        • Sellers must respond to inquiries in good faith
        • We facilitate connections but are not party to transactions

        5. Privacy & Data Protection

        • We collect and process personal data as per our Privacy Policy
        • User data is protected with industry-standard security measures
        • We do not sell personal information to third parties
        • Users can request data deletion by contacting support

        6. Prohibited Activities

        • Posting false or misleading property information
        • Harassment or inappropriate communication
        • Attempting to bypass the platform for transactions
        • Using automated tools to scrape data
        • Violating applicable laws and regulations

        7. Limitation of Liability

        • We provide the platform 'as is' without warranties
        • We are not responsible for property transaction disputes
        • Users must verify property details independently
        • Our liability is limited to the extent permitted by law

        8. Modifications to Terms

        • We reserve the right to modify these terms at any time
        • Users will be notified of significant changes
        • Continued use constitutes acceptance of modified terms

        9. Termination

        • We may terminate accounts for violations of these terms
        • Users may delete their accounts at any time
        • Termination does not affect existing legal obligations

        10. Contact Information

        For questions about these Terms & Conditions, please contact us at:

        Email: support@propertylistingapp.com
        Phone: +91 98765 43210
        Address: Property Listing App, India

        By using our app, you acknowledge that you have read, understood, and agree to be bound by these Terms and Conditions.
        """
        
        termsTextView.text = termsText
    }
    
    @objc private func cancelTapped() {
        dismiss(animated: true)
    }
    
    @IBAction func acceptButtonTapped(_ sender: UIButton) {
        dismiss(animated: true)
    }
}
