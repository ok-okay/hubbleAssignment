package com.example.demo.LoginPage.utils;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class LoginJwtUtil {
	@Value("${app.jwt.exp}")
    private String EXPIRE_DURATION;
    
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;
    
    public String generateAccessToken(String userId, String identifier) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", userId, identifier))
                .setIssuer("Hubble")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Integer.parseInt(EXPIRE_DURATION)))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    
    public String getUserId(String token) {
    	String[] chunks = token.split("\\.");
    	Base64.Decoder decoder = Base64.getUrlDecoder();
    	String payload = new String(decoder.decode(chunks[1]));
    	payload = payload.split(",")[0];
		String userId = payload.substring(8, payload.length());
    	return userId;
    }
    
}
