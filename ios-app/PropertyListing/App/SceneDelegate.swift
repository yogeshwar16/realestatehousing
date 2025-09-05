import UIKit

class SceneDelegate: UIResponder, UIWindowSceneDelegate {

    var window: UIWindow?

    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        guard let windowScene = (scene as? UIWindowScene) else { return }
        
        window = UIWindow(windowScene: windowScene)
        
        // Check if user is logged in
        let isLoggedIn = UserDefaults.standard.bool(forKey: "isLoggedIn")
        
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        
        if isLoggedIn {
            // Navigate to main tab bar controller
            let mainTabBarController = storyboard.instantiateViewController(withIdentifier: "MainTabBarController")
            window?.rootViewController = mainTabBarController
        } else {
            // Navigate to splash/onboarding
            let splashViewController = storyboard.instantiateViewController(withIdentifier: "SplashViewController")
            let navigationController = UINavigationController(rootViewController: splashViewController)
            navigationController.isNavigationBarHidden = true
            window?.rootViewController = navigationController
        }
        
        window?.makeKeyAndVisible()
    }

    func sceneDidDisconnect(_ scene: UIScene) {
        // Called as the scene is being released by the system.
    }

    func sceneDidBecomeActive(_ scene: UIScene) {
        // Called when the scene has moved from an inactive state to an active state.
    }

    func sceneWillResignActive(_ scene: UIScene) {
        // Called when the scene will move from an active state to an inactive state.
    }

    func sceneWillEnterForeground(_ scene: UIScene) {
        // Called as the scene transitions from the background to the foreground.
    }

    func sceneDidEnterBackground(_ scene: UIScene) {
        // Called as the scene transitions from the foreground to the background.
    }
}
