import Foundation
import UIKit

// MARK: - API Service
class APIService {
    static let shared = APIService()
    
    private let baseURL = "http://localhost:8080" // Change this to your server URL
    private let session = URLSession.shared
    
    private init() {}
    
    // MARK: - Authentication APIs
    
    func signup(request: SignupRequest, completion: @escaping (Result<APIResponse<User>, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/auth/signup") else {
            completion(.failure(APIError.invalidURL))
            return
        }
        
        performRequest(url: url, method: "POST", body: request, completion: completion)
    }
    
    func sendOTP(request: OTPRequest, completion: @escaping (Result<APIResponse<String>, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/auth/send-otp") else {
            completion(.failure(APIError.invalidURL))
            return
        }
        
        performRequest(url: url, method: "POST", body: request, completion: completion)
    }
    
    func login(request: LoginRequest, completion: @escaping (Result<APIResponse<User>, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/auth/login") else {
            completion(.failure(APIError.invalidURL))
            return
        }
        
        performRequest(url: url, method: "POST", body: request, completion: completion)
    }
    
    func getUserByMobileNumber(_ mobileNumber: String, completion: @escaping (Result<APIResponse<User>, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/auth/user/\(mobileNumber)") else {
            completion(.failure(APIError.invalidURL))
            return
        }
        
        performRequest(url: url, method: "GET", completion: completion)
    }
    
    func updateUser(mobileNumber: String, request: UserUpdateRequest, completion: @escaping (Result<APIResponse<User>, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/auth/user/\(mobileNumber)") else {
            completion(.failure(APIError.invalidURL))
            return
        }
        
        performRequest(url: url, method: "PUT", body: request, completion: completion)
    }
    
    // MARK: - Property APIs
    
    func getProperties(type: String? = nil, city: String? = nil, search: String? = nil, completion: @escaping (Result<APIResponse<[Property]>, Error>) -> Void) {
        var urlComponents = URLComponents(string: "\(baseURL)/api/properties")!
        var queryItems: [URLQueryItem] = []
        
        if let type = type, !type.isEmpty {
            queryItems.append(URLQueryItem(name: "type", value: type))
        }
        if let city = city, !city.isEmpty {
            queryItems.append(URLQueryItem(name: "city", value: city))
        }
        if let search = search, !search.isEmpty {
            queryItems.append(URLQueryItem(name: "search", value: search))
        }
        
        urlComponents.queryItems = queryItems.isEmpty ? nil : queryItems
        
        guard let url = urlComponents.url else {
            completion(.failure(APIError.invalidURL))
            return
        }
        
        performRequest(url: url, method: "GET", completion: completion)
    }
    
    func getPropertyById(_ id: Int, completion: @escaping (Result<APIResponse<Property>, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/properties/\(id)") else {
            completion(.failure(APIError.invalidURL))
            return
        }
        
        performRequest(url: url, method: "GET", completion: completion)
    }
    
    func createProperty(sellerId: Int, request: PropertyRequest, completion: @escaping (Result<APIResponse<Property>, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/properties/create/\(sellerId)") else {
            completion(.failure(APIError.invalidURL))
            return
        }
        
        performRequest(url: url, method: "POST", body: request, completion: completion)
    }
    
    // MARK: - Inquiry APIs
    
    func createInquiry(token: String, request: InquiryRequest, completion: @escaping (Result<APIResponse<String>, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/api/inquiries") else {
            completion(.failure(APIError.invalidURL))
            return
        }
        
        var urlRequest = URLRequest(url: url)
        urlRequest.httpMethod = "POST"
        urlRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
        urlRequest.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        
        do {
            urlRequest.httpBody = try JSONEncoder().encode(request)
        } catch {
            completion(.failure(error))
            return
        }
        
        session.dataTask(with: urlRequest) { data, response, error in
            DispatchQueue.main.async {
                if let error = error {
                    completion(.failure(error))
                    return
                }
                
                guard let data = data else {
                    completion(.failure(APIError.noData))
                    return
                }
                
                do {
                    let result = try JSONDecoder().decode(APIResponse<String>.self, from: data)
                    completion(.success(result))
                } catch {
                    completion(.failure(error))
                }
            }
        }.resume()
    }
    
    // MARK: - Generic Request Method
    
    private func performRequest<T: Codable, U: Codable>(
        url: URL,
        method: String,
        body: T? = nil,
        completion: @escaping (Result<APIResponse<U>, Error>) -> Void
    ) {
        var request = URLRequest(url: url)
        request.httpMethod = method
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        if let body = body {
            do {
                request.httpBody = try JSONEncoder().encode(body)
            } catch {
                completion(.failure(error))
                return
            }
        }
        
        session.dataTask(with: request) { data, response, error in
            DispatchQueue.main.async {
                if let error = error {
                    completion(.failure(error))
                    return
                }
                
                guard let data = data else {
                    completion(.failure(APIError.noData))
                    return
                }
                
                do {
                    let result = try JSONDecoder().decode(APIResponse<U>.self, from: data)
                    completion(.success(result))
                } catch {
                    completion(.failure(error))
                }
            }
        }.resume()
    }
    
    private func performRequest<U: Codable>(
        url: URL,
        method: String,
        completion: @escaping (Result<APIResponse<U>, Error>) -> Void
    ) {
        var request = URLRequest(url: url)
        request.httpMethod = method
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        session.dataTask(with: request) { data, response, error in
            DispatchQueue.main.async {
                if let error = error {
                    completion(.failure(error))
                    return
                }
                
                guard let data = data else {
                    completion(.failure(APIError.noData))
                    return
                }
                
                do {
                    let result = try JSONDecoder().decode(APIResponse<U>.self, from: data)
                    completion(.success(result))
                } catch {
                    completion(.failure(error))
                }
            }
        }.resume()
    }
}

// MARK: - API Error Enum
enum APIError: Error, LocalizedError {
    case invalidURL
    case noData
    case decodingError
    case networkError(String)
    
    var errorDescription: String? {
        switch self {
        case .invalidURL:
            return "Invalid URL"
        case .noData:
            return "No data received"
        case .decodingError:
            return "Failed to decode response"
        case .networkError(let message):
            return "Network error: \(message)"
        }
    }
}
