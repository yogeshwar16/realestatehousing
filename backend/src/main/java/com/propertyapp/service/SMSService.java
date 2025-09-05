package com.propertyapp.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SMSService {
    
    @Value("${twilio.account-sid}")
    private String accountSid;
    
    @Value("${twilio.auth-token}")
    private String authToken;
    
    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;
    
    public void sendOTP(String mobileNumber, String otp) {
        try {
            Twilio.init(accountSid, authToken);
            
            String messageBody = "Your Property App verification code is: " + otp + 
                               ". This code will expire in 10 minutes. Do not share this code with anyone.";
            
            Message message = Message.creator(
                    new PhoneNumber("+91" + mobileNumber), // Indian mobile number
                    new PhoneNumber(twilioPhoneNumber),
                    messageBody
            ).create();
            
            System.out.println("SMS sent successfully. SID: " + message.getSid());
            
        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
            throw new RuntimeException("SMS sending failed", e);
        }
    }
    
    public void sendNotification(String mobileNumber, String message) {
        try {
            Twilio.init(accountSid, authToken);
            
            Message sms = Message.creator(
                    new PhoneNumber("+91" + mobileNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    message
            ).create();
            
            System.out.println("Notification sent successfully. SID: " + sms.getSid());
            
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
            throw new RuntimeException("Notification sending failed", e);
        }
    }
}
