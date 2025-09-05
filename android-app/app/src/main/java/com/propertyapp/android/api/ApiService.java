package com.propertyapp.android.api;

import com.propertyapp.android.model.Property;
import com.propertyapp.android.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    
    // Authentication APIs
    @POST("auth/signup")
    Call<ApiResponse<User>> signup(@Body SignupRequest request);
    
    @POST("auth/send-otp")
    Call<ApiResponse<String>> sendOTP(@Body OTPRequest request);
    
    @POST("auth/login")
    Call<ApiResponse<User>> login(@Body LoginRequest request);
    
    @GET("auth/user/{mobileNumber}")
    Call<ApiResponse<User>> getUserByMobileNumber(@Path("mobileNumber") String mobileNumber);
    
    // Property APIs
    @GET("api/properties")
    Call<ApiResponse<List<Property>>> getProperties(
        @Query("type") String type,
        @Query("city") String city,
        @Query("search") String search
    );
    
    @GET("properties/seller/{sellerId}")
    Call<ApiResponse<List<Property>>> getPropertiesBySeller(@Path("sellerId") Long sellerId);
    
    @GET("properties/{propertyId}")
    Call<ApiResponse<Property>> getPropertyById(@Path("propertyId") Long propertyId);
    
    @POST("properties/create/{sellerId}")
    Call<ApiResponse<Property>> createProperty(@Path("sellerId") Long sellerId, @Body PropertyRequest request);

    // Inquiry APIs
    @POST("api/inquiries")
    Call<ApiResponse<Object>> createInquiry(
        @Header("Authorization") String token,
        @Body InquiryRequest request
    );
}
