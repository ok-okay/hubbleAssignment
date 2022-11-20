package com.example.demo.LoginPage.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;

import com.example.demo.LoginPage.model.AuthEntity;
import com.example.demo.LoginPage.repository.AuthRepository;
import com.example.demo.LoginPage.utils.LoginCookieUtil;
import com.example.demo.LoginPage.utils.LoginJwtUtil;
import com.example.demo.LoginPage.validation.LoginValidation;

@Service
public class LoginPageService {
	@Autowired
	AuthRepository authRepo;
	
	@Value("${app.baseUrl}")
	private String BASE_URL;
    
    @Autowired
    LoginValidation validator;
    
    @Autowired
    LoginCookieUtil cookieUtil;
    @Autowired
    LoginJwtUtil jwtUtil;
	
	public static String getRandomOtp() {
	    Random rnd = new Random();
	    int number = rnd.nextInt(999999);

	    return String.format("%06d", number);
	}
	
	public void handleGenerateOtp(AuthEntity curAuth) {
		AuthEntity getAuth = authRepo.findByIdentifier(curAuth.getIdentifier());
		if(getAuth!=null) {
			System.out.println(getAuth.getOtp());
		}
		else {
			AuthEntity auth = new AuthEntity();
			auth.setIdentifier(curAuth.getIdentifier());
			
			auth.setMedium(curAuth.getMedium());
			
			if(curAuth.getMedium().toString().equals("PHONE")) {
				validator.phoneValidation(curAuth.getIdentifier());
			}
			if(curAuth.getMedium().toString().equals("EMAIL")) {
				validator.emailValidation(curAuth.getIdentifier());
			}
			
			String otp = getRandomOtp();			
			auth.setOtp(otp);
			System.out.println(otp);
			
			LocalDateTime updatedTime;
			LocalDateTime now = LocalDateTime.now();
			
			updatedTime = now.plusMinutes(5);
			
			auth.setExpiryDate(updatedTime);	
			
			authRepo.save(auth);			
		}
			
	}
	
	public Map<String, String> authenticateUser(AuthEntity curAuth) {
		Map<String, String> res = new HashMap<>();
		AuthEntity auth = authRepo.findByIdentifier(curAuth.getIdentifier());
		if(auth==null) {
			res.put("msg", "User doesn't exist, please try again");
			res.put("statusCode", "404");
		}
		else {
			if(auth.getOtp().equals(curAuth.getOtp())) {
				LocalDateTime now = LocalDateTime.now();
				if(now.isBefore(auth.getExpiryDate())) {
					res.put("msg", "User authorized successfully!");
					res.put("statusCode", "200");
					authRepo.delete(auth);
				}
				else {
					res.put("msg", "OTP expired, please try again");
					res.put("statusCode", "404");
					authRepo.delete(auth);
				}
			}
			else {
				res.put("msg", "Invalid OTP, please try again!");
				res.put("statusCode", "401");
			}
		}
		return res;
	}
	
	public String findOrCreateUser(AuthEntity auth) {
		String url = BASE_URL+"users";
		Map<String, String> data = new HashMap<>();
		data.put("identifier", auth.getIdentifier());
		data.put("medium", auth.getMedium().toString());
		RestTemplate restTemplate = new RestTemplate();
		String userId = restTemplate.postForObject(url, data, String.class);
		return userId;
	}
	
	public String generateOtpRedirect(HttpServletRequest request) {
		String jwtToken = cookieUtil.readCookie(request);
		if(jwtToken.equals("Not found!")) {
			return ("loginPage/generateOTP");			
		}
		else {
			String userId = jwtUtil.getUserId(jwtToken);
			return ("redirect:"+BASE_URL+"users/"+userId);
		}
	}
	
	public String signInStatusCodeRedirect(Map<String, String> res) {
		if(res.get("statusCode").equals("404")) {
			return ("redirect:/");
		}
		else {
			if(res.get("statusCode").equals("401")) {
				return ("redirect:/auth/verify");
			}
		}
		return "redirect:"+BASE_URL;
	}
	
}
