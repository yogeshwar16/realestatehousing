import Foundation

// MARK: - Inquiry Model
struct Inquiry: Codable {
    let id: Int?
    let property: Property?
    let customer: User?
    let seller: User?
    let inquiryDescription: String?
    let status: InquiryStatus
    let termsAccepted: Bool
    let createdAt: String?
    let updatedAt: String?
    let expiryDate: String?
    
    enum CodingKeys: String, CodingKey {
        case id
        case property
        case customer
        case seller
        case inquiryDescription = "inquiry_description"
        case status
        case termsAccepted = "terms_accepted"
        case createdAt = "created_at"
        case updatedAt = "updated_at"
        case expiryDate = "expiry_date"
    }
}

// MARK: - Inquiry Status Enum
enum InquiryStatus: String, Codable, CaseIterable {
    case open = "OPEN"
    case inProgress = "IN_PROGRESS"
    case closed = "CLOSED"
    case expired = "EXPIRED"
    
    var displayName: String {
        switch self {
        case .open:
            return "Open"
        case .inProgress:
            return "In Progress"
        case .closed:
            return "Closed"
        case .expired:
            return "Expired"
        }
    }
    
    var color: String {
        switch self {
        case .open:
            return "SystemBlue"
        case .inProgress:
            return "SystemOrange"
        case .closed:
            return "SystemGreen"
        case .expired:
            return "SystemRed"
        }
    }
}

// MARK: - Inquiry Request Model
struct InquiryRequest: Codable {
    let propertyId: Int
    let inquiryDescription: String?
    let termsAccepted: Bool
    
    enum CodingKeys: String, CodingKey {
        case propertyId = "property_id"
        case inquiryDescription = "inquiry_description"
        case termsAccepted = "terms_accepted"
    }
}
