import Foundation

// MARK: - User Manager for Session Management
class UserManager {
    static let shared = UserManager()
    
    private let userDefaults = UserDefaults.standard
    private let userKey = "currentUser"
    private let loginStatusKey = "isLoggedIn"
    
    private init() {}
    
    // MARK: - User Session Management
    
    var isLoggedIn: Bool {
        return userDefaults.bool(forKey: loginStatusKey)
    }
    
    var currentUser: User? {
        get {
            guard let userData = userDefaults.data(forKey: userKey) else { return nil }
            return try? JSONDecoder().decode(User.self, from: userData)
        }
        set {
            if let user = newValue {
                let userData = try? JSONEncoder().encode(user)
                userDefaults.set(userData, forKey: userKey)
                userDefaults.set(true, forKey: loginStatusKey)
            } else {
                userDefaults.removeObject(forKey: userKey)
                userDefaults.set(false, forKey: loginStatusKey)
            }
        }
    }
    
    func login(user: User) {
        currentUser = user
    }
    
    func logout() {
        currentUser = nil
        // Clear all user-related data
        userDefaults.removeObject(forKey: userKey)
        userDefaults.set(false, forKey: loginStatusKey)
        
        // Post logout notification
        NotificationCenter.default.post(name: .userDidLogout, object: nil)
    }
    
    func updateUser(_ user: User) {
        currentUser = user
    }
}

// MARK: - Notification Names
extension Notification.Name {
    static let userDidLogin = Notification.Name("userDidLogin")
    static let userDidLogout = Notification.Name("userDidLogout")
    static let userDidUpdate = Notification.Name("userDidUpdate")
}
