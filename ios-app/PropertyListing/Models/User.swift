import Foundation

// MARK: - User Model
struct User: Codable {
    let userId: Int?
    let fullName: String
    let mobileNumber: String
    let email: String
    let aadhaarNumber: String?
    let panCard: String?
    let userType: UserType
    let address: String?
    let isActive: Bool?
    let createdAt: String?
    let updatedAt: String?
    
    enum CodingKeys: String, CodingKey {
        case userId = "user_id"
        case fullName = "full_name"
        case mobileNumber = "mobile_number"
        case email
        case aadhaarNumber = "aadhaar_number"
        case panCard = "pan_card"
        case userType = "user_type"
        case address
        case isActive = "is_active"
        case createdAt = "created_at"
        case updatedAt = "updated_at"
    }
}

// MARK: - User Type Enum
enum UserType: String, Codable, CaseIterable {
    case seller = "SELLER"
    case customer = "CUSTOMER"
    
    var displayName: String {
        switch self {
        case .seller:
            return "Seller"
        case .customer:
            return "Customer"
        }
    }
}

// MARK: - User Request Models
struct SignupRequest: Codable {
    let fullName: String
    let mobileNumber: String
    let email: String
    let aadhaarNumber: String
    let panCard: String
    let userType: UserType
    let address: String?
    
    enum CodingKeys: String, CodingKey {
        case fullName = "full_name"
        case mobileNumber = "mobile_number"
        case email
        case aadhaarNumber = "aadhaar_number"
        case panCard = "pan_card"
        case userType = "user_type"
        case address
    }
}

struct LoginRequest: Codable {
    let mobileNumber: String
    let otp: String
    
    enum CodingKeys: String, CodingKey {
        case mobileNumber = "mobile_number"
        case otp
    }
}

struct OTPRequest: Codable {
    let mobileNumber: String
    
    enum CodingKeys: String, CodingKey {
        case mobileNumber = "mobile_number"
    }
}

struct UserUpdateRequest: Codable {
    let fullName: String
    let email: String
    let aadhaarNumber: String?
    let panCard: String?
    let address: String?
    
    enum CodingKeys: String, CodingKey {
        case fullName = "full_name"
        case email
        case aadhaarNumber = "aadhaar_number"
        case panCard = "pan_card"
        case address
    }
}
