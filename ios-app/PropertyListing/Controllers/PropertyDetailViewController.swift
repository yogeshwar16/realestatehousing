import UIKit

class PropertyDetailViewController: UIViewController {
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var propertyImageView: UIImageView!
    @IBOutlet weak var propertyTypeLabel: UILabel!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var locationLabel: UILabel!
    @IBOutlet weak var sizeLabel: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!
    @IBOutlet weak var sellerInfoView: UIView!
    @IBOutlet weak var sellerNameLabel: UILabel!
    @IBOutlet weak var sellerContactLabel: UILabel!
    @IBOutlet weak var inquireButton: UIButton!
    @IBOutlet weak var loadingIndicator: UIActivityIndicatorView!
    
    var property: Property!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        configureWithProperty()
    }
    
    private func setupUI() {
        title = "Property Details"
        navigationController?.navigationBar.prefersLargeTitles = false
        
        // Property image
        propertyImageView.contentMode = .scaleAspectFill
        propertyImageView.clipsToBounds = true
        propertyImageView.roundCorners(radius: 16)
        propertyImageView.backgroundColor = .systemGray6
        
        // Property type label
        propertyTypeLabel.font = UIFont.systemFont(ofSize: 14, weight: .semibold)
        propertyTypeLabel.textColor = .white
        propertyTypeLabel.backgroundColor = .primaryColor
        propertyTypeLabel.textAlignment = .center
        propertyTypeLabel.roundCorners(radius: 12)
        propertyTypeLabel.clipsToBounds = true
        propertyTypeLabel.contentEdgeInsets = UIEdgeInsets(top: 6, left: 12, bottom: 6, right: 12)
        
        // Title label
        titleLabel.font = UIFont.systemFont(ofSize: 24, weight: .bold)
        titleLabel.textColor = .label
        titleLabel.numberOfLines = 0
        
        // Price label
        priceLabel.font = UIFont.systemFont(ofSize: 28, weight: .bold)
        priceLabel.textColor = .primaryColor
        
        // Location label
        locationLabel.font = UIFont.systemFont(ofSize: 16, weight: .medium)
        locationLabel.textColor = .secondaryLabel
        locationLabel.numberOfLines = 0
        
        // Size label
        sizeLabel.font = UIFont.systemFont(ofSize: 14, weight: .medium)
        sizeLabel.textColor = .tertiaryLabel
        
        // Description label
        descriptionLabel.font = UIFont.systemFont(ofSize: 16, weight: .regular)
        descriptionLabel.textColor = .label
        descriptionLabel.numberOfLines = 0
        
        // Seller info view
        sellerInfoView.backgroundColor = .systemGray6
        sellerInfoView.roundCorners(radius: 12)
        
        sellerNameLabel.font = UIFont.systemFont(ofSize: 18, weight: .semibold)
        sellerNameLabel.textColor = .label
        
        sellerContactLabel.font = UIFont.systemFont(ofSize: 16, weight: .medium)
        sellerContactLabel.textColor = .primaryColor
        
        // Inquire button
        inquireButton.setTitle("Inquire About This Property", for: .normal)
        inquireButton.setTitleColor(.white, for: .normal)
        inquireButton.backgroundColor = .secondaryColor
        inquireButton.titleLabel?.font = UIFont.systemFont(ofSize: 18, weight: .semibold)
        inquireButton.roundCorners(radius: 12)
        inquireButton.addShadow()
        
        // Loading indicator
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.color = .primaryColor
    }
    
    private func configureWithProperty() {
        guard let property = property else { return }
        
        titleLabel.text = property.title
        priceLabel.text = property.price.formatAsCurrency()
        locationLabel.text = "\(property.address)\n\(property.city), \(property.state) - \(property.pincode)"
        sizeLabel.text = "\(property.propertySize.formatWithCommas()) sq ft"
        propertyTypeLabel.text = property.propertyType.displayName
        descriptionLabel.text = property.description ?? "No description available"
        
        // Seller information
        if let seller = property.seller {
            sellerNameLabel.text = seller.fullName
            sellerContactLabel.text = seller.mobileNumber
        } else {
            sellerNameLabel.text = "Seller information not available"
            sellerContactLabel.text = "Contact via inquiry"
        }
        
        // Load property image
        if let imageURLString = property.propertyImages,
           let imageURL = URL(string: imageURLString) {
            loadImage(from: imageURL)
        } else {
            propertyImageView.image = UIImage(systemName: property.propertyType.icon)
            propertyImageView.tintColor = .systemGray3
        }
        
        // Configure inquire button based on user type and ownership
        configureInquireButton()
    }
    
    private func configureInquireButton() {
        guard let currentUser = UserManager.shared.currentUser else {
            inquireButton.setTitle("Login to Inquire", for: .normal)
            inquireButton.isEnabled = false
            inquireButton.backgroundColor = .systemGray4
            return
        }
        
        // Check if user is a customer
        if currentUser.userType != .customer {
            inquireButton.setTitle("Only Customers Can Inquire", for: .normal)
            inquireButton.isEnabled = false
            inquireButton.backgroundColor = .systemGray4
            return
        }
        
        // Check if user is trying to inquire about their own property
        if let seller = property.seller,
           seller.userId == currentUser.userId {
            inquireButton.setTitle("You Cannot Inquire About Your Own Property", for: .normal)
            inquireButton.isEnabled = false
            inquireButton.backgroundColor = .systemGray4
            return
        }
        
        // Enable inquire button
        inquireButton.setTitle("Inquire About This Property", for: .normal)
        inquireButton.isEnabled = true
        inquireButton.backgroundColor = .secondaryColor
    }
    
    private func loadImage(from url: URL) {
        URLSession.shared.dataTask(with: url) { [weak self] data, response, error in
            guard let data = data, let image = UIImage(data: data) else { return }
            
            DispatchQueue.main.async {
                self?.propertyImageView.image = image
            }
        }.resume()
    }
    
    @IBAction func inquireButtonTapped(_ sender: UIButton) {
        guard UserManager.shared.isLoggedIn,
              let currentUser = UserManager.shared.currentUser,
              currentUser.userType == .customer else {
            showAlert(title: "Error", message: "Please login as a customer to make inquiries")
            return
        }
        
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let inquiryVC = storyboard.instantiateViewController(withIdentifier: "InquiryViewController") as! InquiryViewController
        inquiryVC.property = property
        
        let navController = UINavigationController(rootViewController: inquiryVC)
        present(navController, animated: true)
    }
}
