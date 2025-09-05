import UIKit

class PropertyCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var propertyImageView: UIImageView!
    @IBOutlet weak var propertyTypeLabel: UILabel!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var locationLabel: UILabel!
    @IBOutlet weak var sizeLabel: UILabel!
    @IBOutlet weak var inquireButton: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        setupUI()
    }
    
    private func setupUI() {
        // Container view
        containerView.backgroundColor = .cardBackground
        containerView.roundCorners(radius: 16)
        containerView.addShadow(color: .black, opacity: 0.1, offset: CGSize(width: 0, height: 4), radius: 8)
        
        // Property image
        propertyImageView.contentMode = .scaleAspectFill
        propertyImageView.clipsToBounds = true
        propertyImageView.roundCorners(radius: 12)
        propertyImageView.backgroundColor = .systemGray6
        
        // Property type label
        propertyTypeLabel.font = UIFont.systemFont(ofSize: 12, weight: .semibold)
        propertyTypeLabel.textColor = .white
        propertyTypeLabel.backgroundColor = .primaryColor
        propertyTypeLabel.textAlignment = .center
        propertyTypeLabel.roundCorners(radius: 8)
        propertyTypeLabel.clipsToBounds = true
        
        // Title label
        titleLabel.font = UIFont.systemFont(ofSize: 16, weight: .semibold)
        titleLabel.textColor = .label
        titleLabel.numberOfLines = 2
        
        // Price label
        priceLabel.font = UIFont.systemFont(ofSize: 18, weight: .bold)
        priceLabel.textColor = .primaryColor
        
        // Location label
        locationLabel.font = UIFont.systemFont(ofSize: 14, weight: .medium)
        locationLabel.textColor = .secondaryLabel
        locationLabel.numberOfLines = 1
        
        // Size label
        sizeLabel.font = UIFont.systemFont(ofSize: 12, weight: .medium)
        sizeLabel.textColor = .tertiaryLabel
        
        // Inquire button
        inquireButton.setTitle("Inquire", for: .normal)
        inquireButton.setTitleColor(.white, for: .normal)
        inquireButton.backgroundColor = .secondaryColor
        inquireButton.titleLabel?.font = UIFont.systemFont(ofSize: 14, weight: .semibold)
        inquireButton.roundCorners(radius: 8)
        inquireButton.contentEdgeInsets = UIEdgeInsets(top: 6, left: 12, bottom: 6, right: 12)
    }
    
    func configure(with property: Property) {
        titleLabel.text = property.title
        priceLabel.text = property.price.formatAsCurrency()
        locationLabel.text = "\(property.city), \(property.state)"
        sizeLabel.text = "\(property.propertySize.formatWithCommas()) sq ft"
        propertyTypeLabel.text = property.propertyType.displayName
        
        // Load property image
        if let imageURLString = property.propertyImages,
           let imageURL = URL(string: imageURLString) {
            loadImage(from: imageURL)
        } else {
            propertyImageView.image = UIImage(systemName: property.propertyType.icon)
            propertyImageView.tintColor = .systemGray3
        }
        
        // Hide inquire button for sellers viewing their own properties
        if let currentUser = UserManager.shared.currentUser,
           currentUser.userType == .seller,
           property.seller?.userId == currentUser.userId {
            inquireButton.isHidden = true
        } else {
            inquireButton.isHidden = false
        }
    }
    
    private func loadImage(from url: URL) {
        // Simple image loading - in production, use a proper image loading library like SDWebImage
        URLSession.shared.dataTask(with: url) { [weak self] data, response, error in
            guard let data = data, let image = UIImage(data: data) else { return }
            
            DispatchQueue.main.async {
                self?.propertyImageView.image = image
            }
        }.resume()
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        propertyImageView.image = nil
        inquireButton.isHidden = false
    }
}
