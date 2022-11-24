package com.example.demo.LoginPage.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    
	public String getRandomOtp() {
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
			if(curAuth.getOtp().equals(auth.getOtp())) {
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
		String jwtToken = cookieUtil.readUserCookie(request);
		if(jwtToken.equals("Not found!")) {
			return ("loginPage/generateOTP");			
		}
		else {
			String userId = jwtUtil.extractJwtData(jwtToken).get("userId");
			return ("redirect:"+BASE_URL+"users/"+userId);
		}
	}
	
	public Map<String, String> authScreenData(Optional<String> medium, String identifier){
		Map<String, String> data = new HashMap<>();
		if(medium.isEmpty()) {
			data.put("medium", "PHONE");
			data.put("submit", "SignIn/SignUp");
		}
		else {
			data.put("medium", medium.get());
			data.put("submit", "Update "+medium.get());
		}
		data.put("identifier", identifier);
		data.put("msg", "");
		return data;
	}
	
	public Map<String, String> userLoginData(HttpServletResponse response, String userToken, AuthEntity auth, Map<String, String> res){
		Map<String, String> data = new HashMap<>();
		if(userToken.equals("Not found!")) {
			if(res.get("statusCode").equals("200")) {
				String userId = findOrCreateUser(auth);
				userToken = jwtUtil.generateUserAccessToken(userId, auth.getIdentifier());
				cookieUtil.createUserCookie(response, userToken);
			    data.put("view", "redirect:"+BASE_URL+"users/"+userId);
			    data.put("msg", "");
			}
			else {
				if(res.get("statusCode").equals("404")) {
					data.put("view", "redirect:"+BASE_URL);
				}
				if(res.get("statusCode").equals("401")) {
					String redirectUrl = "redirect:"+BASE_URL+"auth/verify";
					redirectUrl+="?identifier="+auth.getIdentifier();
					data.put("view", redirectUrl);
				}
				data.put("msg", res.get("msg"));
			}
		}
		else {
			Boolean authorized = jwtUtil.validateAccessToken(userToken);
			if(authorized) {
				if(res.get("statusCode").equals("200")) {
					String userId = jwtUtil.extractJwtData(userToken).get("userId");
					String updateToken = jwtUtil.generateUpdateAccessToken(userId, auth.getIdentifier(), auth.getMedium().toString());
					data.put("view", "redirect:"+BASE_URL+"users/"+userId+"?updateToken="+updateToken);									
					data.put("msg", "");
				}
				else {
					
					if(res.get("statusCode").equals("404")) {
						data.put("view", "redirect:"+BASE_URL);
					}
					if(res.get("statusCode").equals("401")) {
						String redirectUrl = "redirect:"+BASE_URL+"auth/verify";
						redirectUrl+="?identifier="+auth.getIdentifier();
						redirectUrl+="&medium="+auth.getMedium();
						redirectUrl+="&submit=Update"+auth.getMedium();
						data.put("view", redirectUrl);
					}
					data.put("msg", res.get("msg"));
				}

			}
			else {
				cookieUtil.deleteUserCookie(response);
				data.put("view", "redirect:"+BASE_URL);
			}

		}
		return data;
	}
	
}
