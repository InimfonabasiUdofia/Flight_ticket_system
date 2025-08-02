package com.airline.database;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class OTPService {
    private static final SecureRandom random = new SecureRandom();
    private Map<String, OTPData> otpStore = new ConcurrentHashMap<>();
    
    private static class OTPData {
        String otp;
        LocalDateTime createdAt;
        
        OTPData(String otp) {
            this.otp = otp;
            this.createdAt = LocalDateTime.now();
        }
    }
    
    public String generateOTP(String email) {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10));
        }
        
        String otpCode = otp.toString();
        otpStore.put(email.toLowerCase(), new OTPData(otpCode));
        return otpCode;
    }
    
    public boolean validateOTP(String email, String inputOTP) {
        OTPData otpData = otpStore.get(email.toLowerCase());
        
        if (otpData == null) return false;
        
        if (ChronoUnit.MINUTES.between(otpData.createdAt, LocalDateTime.now()) > 5) {
            otpStore.remove(email.toLowerCase());
            return false;
        }
        
        if (otpData.otp.equals(inputOTP)) {
            otpStore.remove(email.toLowerCase());
            return true;
        }
        return false;
    }
}