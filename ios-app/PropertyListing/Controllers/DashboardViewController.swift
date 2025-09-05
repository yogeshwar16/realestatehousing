import UIKit

class DashboardViewController: UIViewController {
    
    @IBOutlet weak var searchBar: UISearchBar!
    @IBOutlet weak var filterScrollView: UIScrollView!
    @IBOutlet weak var filterStackView: UIStackView!
    @IBOutlet weak var propertiesCollectionView: UICollectionView!
    @IBOutlet weak var loadingIndicator: UIActivityIndicatorView!
    @IBOutlet weak var emptyStateView: UIView!
    @IBOutlet weak var emptyStateLabel: UILabel!
    
    private var properties: [Property] = []
    private var filteredProperties: [Property] = []
    private var selectedPropertyType: PropertyType?
    private var currentSearchText = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        setupCollectionView()
        setupFilterButtons()
        loadProperties()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        loadProperties() // Refresh properties when returning to dashboard
    }
    
    private func setupUI() {
        title = "Properties"
        navigationController?.navigationBar.prefersLargeTitles = true
        
        // Search bar
        searchBar.delegate = self
        searchBar.placeholder = "Search by location, title..."
        searchBar.searchBarStyle = .minimal
        searchBar.backgroundColor = .clear
        
        // Loading indicator
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.color = .primaryColor
        
        // Empty state
        emptyStateView.isHidden = true
        emptyStateLabel.text = "No properties found\nTry adjusting your search or filters"
        emptyStateLabel.textAlignment = .center
        emptyStateLabel.numberOfLines = 0
        emptyStateLabel.font = UIFont.systemFont(ofSize: 16, weight: .medium)
        emptyStateLabel.textColor = .systemGray
        
        // Add property button for sellers
        if UserManager.shared.currentUser?.userType == .seller {
            let addButton = UIBarButtonItem(
                image: UIImage(systemName: "plus"),
                style: .plain,
                target: self,
                action: #selector(addPropertyTapped)
            )
            navigationItem.rightBarButtonItem = addButton
        }
    }
    
    private func setupCollectionView() {
        propertiesCollectionView.delegate = self
        propertiesCollectionView.dataSource = self
        propertiesCollectionView.backgroundColor = .clear
        
        // Register cell
        let nib = UINib(nibName: "PropertyCollectionViewCell", bundle: nil)
        propertiesCollectionView.register(nib, forCellWithReuseIdentifier: "PropertyCollectionViewCell")
        
        // Layout
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        layout.minimumInteritemSpacing = 16
        layout.minimumLineSpacing = 20
        layout.sectionInset = UIEdgeInsets(top: 16, left: 16, bottom: 16, right: 16)
        propertiesCollectionView.collectionViewLayout = layout
    }
    
    private func setupFilterButtons() {
        // Clear existing buttons
        filterStackView.arrangedSubviews.forEach { $0.removeFromSuperview() }
        
        // All properties button
        let allButton = createFilterButton(title: "All", type: nil)
        allButton.isSelected = selectedPropertyType == nil
        filterStackView.addArrangedSubview(allButton)
        
        // Property type buttons
        for propertyType in PropertyType.allCases {
            let button = createFilterButton(title: propertyType.displayName, type: propertyType)
            button.isSelected = selectedPropertyType == propertyType
            filterStackView.addArrangedSubview(button)
        }
    }
    
    private func createFilterButton(title: String, type: PropertyType?) -> UIButton {
        let button = UIButton(type: .system)
        button.setTitle(title, for: .normal)
        button.titleLabel?.font = UIFont.systemFont(ofSize: 14, weight: .medium)
        button.backgroundColor = .systemGray6
        button.setTitleColor(.systemGray, for: .normal)
        button.setTitleColor(.white, for: .selected)
        button.roundCorners(radius: 20)
        button.contentEdgeInsets = UIEdgeInsets(top: 8, left: 16, bottom: 8, right: 16)
        
        button.addTarget(self, action: #selector(filterButtonTapped(_:)), for: .touchUpInside)
        button.tag = type?.hashValue ?? -1
        
        return button
    }
    
    @objc private func filterButtonTapped(_ sender: UIButton) {
        // Update selection
        filterStackView.arrangedSubviews.forEach { view in
            if let button = view as? UIButton {
                button.isSelected = (button == sender)
                button.backgroundColor = button.isSelected ? .primaryColor : .systemGray6
            }
        }
        
        // Update filter
        if sender.tag == -1 {
            selectedPropertyType = nil
        } else {
            selectedPropertyType = PropertyType.allCases.first { $0.hashValue == sender.tag }
        }
        
        applyFilters()
    }
    
    @objc private func addPropertyTapped() {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let addPropertyVC = storyboard.instantiateViewController(withIdentifier: "AddPropertyViewController")
        let navController = UINavigationController(rootViewController: addPropertyVC)
        present(navController, animated: true)
    }
    
    private func loadProperties() {
        loadingIndicator.startAnimating()
        emptyStateView.isHidden = true
        
        APIService.shared.getProperties { [weak self] result in
            DispatchQueue.main.async {
                self?.loadingIndicator.stopAnimating()
                
                switch result {
                case .success(let response):
                    if response.success, let properties = response.data {
                        self?.properties = properties
                        self?.applyFilters()
                    } else {
                        self?.showError(message: response.message)
                    }
                case .failure(let error):
                    self?.showError(message: error.localizedDescription)
                }
            }
        }
    }
    
    private func applyFilters() {
        filteredProperties = properties
        
        // Apply property type filter
        if let selectedType = selectedPropertyType {
            filteredProperties = filteredProperties.filter { $0.propertyType == selectedType }
        }
        
        // Apply search filter
        if !currentSearchText.isEmpty {
            filteredProperties = filteredProperties.filter { property in
                property.title.localizedCaseInsensitiveContains(currentSearchText) ||
                property.city.localizedCaseInsensitiveContains(currentSearchText) ||
                property.address.localizedCaseInsensitiveContains(currentSearchText)
            }
        }
        
        updateUI()
    }
    
    private func updateUI() {
        propertiesCollectionView.reloadData()
        emptyStateView.isHidden = !filteredProperties.isEmpty
    }
    
    private func showError(message: String) {
        showAlert(title: "Error", message: message)
        emptyStateView.isHidden = false
        emptyStateLabel.text = "Failed to load properties\nPlease try again"
    }
}

// MARK: - UISearchBarDelegate
extension DashboardViewController: UISearchBarDelegate {
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        currentSearchText = searchText
        applyFilters()
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        searchBar.resignFirstResponder()
    }
}

// MARK: - UICollectionViewDataSource
extension DashboardViewController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return filteredProperties.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "PropertyCollectionViewCell", for: indexPath) as! PropertyCollectionViewCell
        let property = filteredProperties[indexPath.item]
        cell.configure(with: property)
        return cell
    }
}

// MARK: - UICollectionViewDelegate
extension DashboardViewController: UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let property = filteredProperties[indexPath.item]
        
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let propertyDetailVC = storyboard.instantiateViewController(withIdentifier: "PropertyDetailViewController") as! PropertyDetailViewController
        propertyDetailVC.property = property
        
        navigationController?.pushViewController(propertyDetailVC, animated: true)
    }
}

// MARK: - UICollectionViewDelegateFlowLayout
extension DashboardViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let padding: CGFloat = 32 // 16 + 16 for left and right
        let availableWidth = collectionView.frame.width - padding
        let cellWidth = (availableWidth - 16) / 2 // 16 for spacing between cells
        return CGSize(width: cellWidth, height: 280)
    }
}
