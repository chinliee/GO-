package com.team4.demo.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class SaltGenerator {

    private static final Integer SaltLength = 16;


	public static String generateSalt() {
//    	宣告加鹽
    	SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[SaltLength];
        random.nextBytes(saltBytes);
        String saltString = Base64.getEncoder().encodeToString(saltBytes);

        return  saltString;
    }


	public static String  hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest((password + salt).getBytes(StandardCharsets.UTF_8));
       
            return  Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("加密時出錯");
        }
    }
}
