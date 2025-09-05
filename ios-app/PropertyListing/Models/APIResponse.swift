import Foundation

// MARK: - Generic API Response Model
struct APIResponse<T: Codable>: Codable {
    let success: Bool
    let message: String
    let data: T?
    let error: String?
    
    enum CodingKeys: String, CodingKey {
        case success
        case message
        case data
        case error
    }
}

// MARK: - Error Response Model
struct ErrorResponse: Codable {
    let error: String
    let message: String
    let timestamp: String?
    let path: String?
}
