package com.example.demo.UserDetails.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class UserCookieUtil {
    @Value("${app.jwt.exp}")
    private String EXPIRE_DURATION;

	public String readCookie(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, "jwtToken");
		if(cookie!=null) {
			return cookie.getValue();
		}
		else {
			return "Not found!";
		}
	}
	
	public void deleteCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("jwtToken", null);
	    cookie.setMaxAge(0);
	    cookie.setPath("/");
	    cookie.setSecure(false);
	    response.addCookie(cookie);
	}	
}
