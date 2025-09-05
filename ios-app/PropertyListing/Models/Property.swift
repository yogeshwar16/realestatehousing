import Foundation

// MARK: - Property Model
struct Property: Codable {
    let id: Int?
    let propertyType: PropertyType
    let title: String
    let description: String?
    let propertySize: Double
    let price: Double
    let address: String
    let city: String
    let state: String
    let pincode: String
    let latitude: Double?
    let longitude: Double?
    let propertyImages: String?
    let ptrDocument: String?
    let seller: User?
    let isActive: Bool?
    let createdAt: String?
    let updatedAt: String?
    
    enum CodingKeys: String, CodingKey {
        case id
        case propertyType = "property_type"
        case title
        case description
        case propertySize = "property_size"
        case price
        case address
        case city
        case state
        case pincode
        case latitude
        case longitude
        case propertyImages = "property_images"
        case ptrDocument = "ptr_document"
        case seller
        case isActive = "is_active"
        case createdAt = "created_at"
        case updatedAt = "updated_at"
    }
}

// MARK: - Property Type Enum
enum PropertyType: String, Codable, CaseIterable {
    case land = "LAND"
    case flat = "FLAT"
    case rowHouse = "ROW_HOUSE"
    case bungalow = "BUNGALOW"
    
    var displayName: String {
        switch self {
        case .land:
            return "Land"
        case .flat:
            return "Flat"
        case .rowHouse:
            return "Row House"
        case .bungalow:
            return "Bungalow"
        }
    }
    
    var icon: String {
        switch self {
        case .land:
            return "map"
        case .flat:
            return "building"
        case .rowHouse:
            return "house"
        case .bungalow:
            return "house.fill"
        }
    }
}

// MARK: - Property Request Model
struct PropertyRequest: Codable {
    let propertyType: PropertyType
    let title: String
    let description: String
    let propertySize: Double
    let price: Double
    let address: String
    let city: String
    let state: String
    let pincode: String
    let latitude: Double?
    let longitude: Double?
    let propertyImages: String?
    let ptrDocument: String?
    
    enum CodingKeys: String, CodingKey {
        case propertyType = "property_type"
        case title
        case description
        case propertySize = "property_size"
        case price
        case address
        case city
        case state
        case pincode
        case latitude
        case longitude
        case propertyImages = "property_images"
        case ptrDocument = "ptr_document"
    }
}
