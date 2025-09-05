import UIKit
import Foundation

// MARK: - UIView Extensions
extension UIView {
    
    func addShadow(color: UIColor = .black, opacity: Float = 0.1, offset: CGSize = CGSize(width: 0, height: 2), radius: CGFloat = 4) {
        layer.shadowColor = color.cgColor
        layer.shadowOpacity = opacity
        layer.shadowOffset = offset
        layer.shadowRadius = radius
        layer.masksToBounds = false
    }
    
    func roundCorners(radius: CGFloat) {
        layer.cornerRadius = radius
        layer.masksToBounds = true
    }
    
    func addBorder(color: UIColor, width: CGFloat) {
        layer.borderColor = color.cgColor
        layer.borderWidth = width
    }
    
    func fadeIn(duration: TimeInterval = 0.3) {
        alpha = 0
        UIView.animate(withDuration: duration) {
            self.alpha = 1
        }
    }
    
    func fadeOut(duration: TimeInterval = 0.3) {
        UIView.animate(withDuration: duration) {
            self.alpha = 0
        }
    }
}

// MARK: - UIColor Extensions
extension UIColor {
    convenience init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        let a, r, g, b: UInt64
        switch hex.count {
        case 3: // RGB (12-bit)
            (a, r, g, b) = (255, (int >> 8) * 17, (int >> 4 & 0xF) * 17, (int & 0xF) * 17)
        case 6: // RGB (24-bit)
            (a, r, g, b) = (255, int >> 16, int >> 8 & 0xFF, int & 0xFF)
        case 8: // ARGB (32-bit)
            (a, r, g, b) = (int >> 24, int >> 16 & 0xFF, int >> 8 & 0xFF, int & 0xFF)
        default:
            (a, r, g, b) = (1, 1, 1, 0)
        }
        
        self.init(
            red: CGFloat(r) / 255,
            green: CGFloat(g) / 255,
            blue: CGFloat(b) / 255,
            alpha: CGFloat(a) / 255
        )
    }
    
    static let primaryColor = UIColor(hex: "#1976D2")
    static let primaryDark = UIColor(hex: "#0D47A1")
    static let primaryLight = UIColor(hex: "#42A5F5")
    static let secondaryColor = UIColor(hex: "#FF6F00")
    static let accentColor = UIColor(hex: "#00BCD4")
    static let successColor = UIColor(hex: "#4CAF50")
    static let warningColor = UIColor(hex: "#FF9800")
    static let errorColor = UIColor(hex: "#F44336")
    static let cardBackground = UIColor(hex: "#FFFFFF")
    static let backgroundLight = UIColor(hex: "#FAFAFA")
}

// MARK: - String Extensions
extension String {
    var isValidEmail: Bool {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        let emailPred = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailPred.evaluate(with: self)
    }
    
    var isValidMobileNumber: Bool {
        let mobileRegEx = "^[6-9]\\d{9}$"
        let mobilePred = NSPredicate(format:"SELF MATCHES %@", mobileRegEx)
        return mobilePred.evaluate(with: self)
    }
    
    var isValidPAN: Bool {
        let panRegEx = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$"
        let panPred = NSPredicate(format:"SELF MATCHES %@", panRegEx)
        return panPred.evaluate(with: self)
    }
    
    var isValidAadhaar: Bool {
        let aadhaarRegEx = "^[0-9]{12}$"
        let aadhaarPred = NSPredicate(format:"SELF MATCHES %@", aadhaarRegEx)
        return aadhaarPred.evaluate(with: self)
    }
    
    func formatAsCurrency() -> String {
        guard let number = Double(self) else { return self }
        let formatter = NumberFormatter()
        formatter.numberStyle = .currency
        formatter.locale = Locale(identifier: "en_IN")
        return formatter.string(from: NSNumber(value: number)) ?? self
    }
}

// MARK: - Double Extensions
extension Double {
    func formatAsCurrency() -> String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .currency
        formatter.locale = Locale(identifier: "en_IN")
        return formatter.string(from: NSNumber(value: self)) ?? "\(self)"
    }
    
    func formatWithCommas() -> String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        formatter.locale = Locale(identifier: "en_IN")
        return formatter.string(from: NSNumber(value: self)) ?? "\(self)"
    }
}

// MARK: - UIViewController Extensions
extension UIViewController {
    
    func showAlert(title: String, message: String, completion: (() -> Void)? = nil) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default) { _ in
            completion?()
        })
        present(alert, animated: true)
    }
    
    func showConfirmationAlert(title: String, message: String, confirmTitle: String = "Confirm", cancelTitle: String = "Cancel", onConfirm: @escaping () -> Void) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: cancelTitle, style: .cancel))
        alert.addAction(UIAlertAction(title: confirmTitle, style: .default) { _ in
            onConfirm()
        })
        present(alert, animated: true)
    }
    
    func showLoadingIndicator() -> UIActivityIndicatorView {
        let indicator = UIActivityIndicatorView(style: .large)
        indicator.color = .primaryColor
        indicator.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(indicator)
        
        NSLayoutConstraint.activate([
            indicator.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            indicator.centerYAnchor.constraint(equalTo: view.centerYAnchor)
        ])
        
        indicator.startAnimating()
        return indicator
    }
    
    func hideKeyboardWhenTappedAround() {
        let tap = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
}

// MARK: - UITextField Extensions
extension UITextField {
    
    func addPadding(left: CGFloat = 0, right: CGFloat = 0) {
        if left > 0 {
            let leftView = UIView(frame: CGRect(x: 0, y: 0, width: left, height: frame.height))
            self.leftView = leftView
            self.leftViewMode = .always
        }
        
        if right > 0 {
            let rightView = UIView(frame: CGRect(x: 0, y: 0, width: right, height: frame.height))
            self.rightView = rightView
            self.rightViewMode = .always
        }
    }
    
    func setPlaceholderColor(_ color: UIColor) {
        attributedPlaceholder = NSAttributedString(
            string: placeholder ?? "",
            attributes: [NSAttributedString.Key.foregroundColor: color]
        )
    }
}

// MARK: - Date Extensions
extension Date {
    func toString(format: String = "dd/MM/yyyy") -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = format
        return formatter.string(from: self)
    }
    
    static func fromString(_ string: String, format: String = "yyyy-MM-dd'T'HH:mm:ss") -> Date? {
        let formatter = DateFormatter()
        formatter.dateFormat = format
        return formatter.date(from: string)
    }
}
