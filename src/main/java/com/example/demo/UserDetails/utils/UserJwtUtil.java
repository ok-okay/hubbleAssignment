package com.example.demo.UserDetails.utils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class UserJwtUtil {
	@Value("${app.jwt.exp}")
    private String EXPIRE_DURATION;
	
	@Value("${app.otp.exp}")
    private String OTP_EXPIRY;
    
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserJwtUtil.class); 

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            LOGGER.error("JWT expired", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Token is null, empty or only whitespace", ex.getMessage());
        } catch (MalformedJwtException ex) {
            LOGGER.error("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("JWT is not supported", ex);
        } catch (SignatureException ex) {
            LOGGER.error("Signature validation failed");
        }
         
        return false;
    }

    public Map<String, String> extractJwtData(String token){
    	Map<String, String> data = new HashMap<>();
    	String[] chunks = token.split("\\.");
    	Base64.Decoder decoder = Base64.getUrlDecoder();
    	String payload = new String(decoder.decode(chunks[1]));
    	payload = payload.substring(8, payload.length());
    	payload = payload.substring(0, payload.indexOf('"'));
    	
    	chunks = payload.split(",");
    	data.put("userId", chunks[0]);
    	data.put("identifier", chunks[1]);
    	if(chunks.length==3) {
        	data.put("medium", chunks[2]);
    	}    	
    	return data;
    }
    
}
