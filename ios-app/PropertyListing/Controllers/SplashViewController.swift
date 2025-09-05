import UIKit

class SplashViewController: UIViewController {
    
    @IBOutlet weak var logoImageView: UIImageView!
    @IBOutlet weak var appNameLabel: UILabel!
    @IBOutlet weak var taglineLabel: UILabel!
    @IBOutlet weak var getStartedButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        animateElements()
    }
    
    private func setupUI() {
        view.backgroundColor = UIColor.primaryColor
        
        // Logo setup
        logoImageView.image = UIImage(systemName: "house.fill")
        logoImageView.tintColor = .white
        logoImageView.contentMode = .scaleAspectFit
        
        // App name label
        appNameLabel.text = "Property Listing"
        appNameLabel.textColor = .white
        appNameLabel.font = UIFont.systemFont(ofSize: 32, weight: .bold)
        appNameLabel.textAlignment = .center
        
        // Tagline label
        taglineLabel.text = "Find Your Dream Property"
        taglineLabel.textColor = UIColor.white.withAlphaComponent(0.8)
        taglineLabel.font = UIFont.systemFont(ofSize: 16, weight: .medium)
        taglineLabel.textAlignment = .center
        
        // Get started button
        getStartedButton.setTitle("Get Started", for: .normal)
        getStartedButton.setTitleColor(.primaryColor, for: .normal)
        getStartedButton.backgroundColor = .white
        getStartedButton.titleLabel?.font = UIFont.systemFont(ofSize: 18, weight: .semibold)
        getStartedButton.roundCorners(radius: 25)
        getStartedButton.addShadow()
        
        // Initially hide elements for animation
        logoImageView.alpha = 0
        appNameLabel.alpha = 0
        taglineLabel.alpha = 0
        getStartedButton.alpha = 0
        getStartedButton.transform = CGAffineTransform(scaleX: 0.8, y: 0.8)
    }
    
    private func animateElements() {
        // Logo animation
        UIView.animate(withDuration: 0.8, delay: 0.2, options: .curveEaseOut) {
            self.logoImageView.alpha = 1
            self.logoImageView.transform = CGAffineTransform(scaleX: 1.1, y: 1.1)
        } completion: { _ in
            UIView.animate(withDuration: 0.3) {
                self.logoImageView.transform = .identity
            }
        }
        
        // App name animation
        UIView.animate(withDuration: 0.6, delay: 0.8, options: .curveEaseOut) {
            self.appNameLabel.alpha = 1
        }
        
        // Tagline animation
        UIView.animate(withDuration: 0.6, delay: 1.2, options: .curveEaseOut) {
            self.taglineLabel.alpha = 1
        }
        
        // Button animation
        UIView.animate(withDuration: 0.8, delay: 1.6, usingSpringWithDamping: 0.7, initialSpringVelocity: 0.5, options: .curveEaseOut) {
            self.getStartedButton.alpha = 1
            self.getStartedButton.transform = .identity
        }
    }
    
    @IBAction func getStartedTapped(_ sender: UIButton) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let signupVC = storyboard.instantiateViewController(withIdentifier: "SignupViewController")
        navigationController?.pushViewController(signupVC, animated: true)
    }
}
