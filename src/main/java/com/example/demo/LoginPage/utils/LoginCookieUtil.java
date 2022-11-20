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
	
	public void createCookie(HttpServletResponse response, String jwtToken) {
		Cookie cookie = new Cookie("jwtToken", jwtToken);
	    cookie.setMaxAge(Integer.parseInt(EXPIRE_DURATION)/1000);
	    cookie.setPath("/");
	    cookie.setSecure(false);
	    response.addCookie(cookie);
	}
	
	public String readCookie(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, "jwtToken");
		if(cookie!=null) {
			return cookie.getValue();
		}
		else {
			return "Not found!";
		}
	}
}
