package com.example.demo.LoginPage.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class LoginCookieUtil {
    @Value("${app.jwt.exp}")
    private String EXPIRE_DURATION;
    @Value("${app.otp.exp}")
    private String OTP_EXPIRY;
	
	public void createUserCookie(HttpServletResponse response, String jwtToken) {
		Cookie cookie = new Cookie("jwtToken", jwtToken);
	    cookie.setMaxAge(Integer.parseInt(EXPIRE_DURATION)/1000);
	    cookie.setPath("/");
	    cookie.setSecure(false);
	    response.addCookie(cookie);
	}
	
	public void createUpdateCookie(HttpServletResponse response, String updateToken) {
		Cookie cookie = new Cookie("updateToken", updateToken);
	    cookie.setMaxAge(Integer.parseInt(OTP_EXPIRY)/1000);
	    cookie.setPath("/");
	    cookie.setSecure(false);
	    response.addCookie(cookie);
	}
	
	public String readUserCookie(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, "jwtToken");
		if(cookie!=null) {
			return cookie.getValue();
		}
		else {
			return "Not found!";
		}
	}
	
	public void deleteUserCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("jwtToken", null);
	    cookie.setMaxAge(0);
	    cookie.setPath("/");
	    cookie.setSecure(false);
	    response.addCookie(cookie);
	}
}
