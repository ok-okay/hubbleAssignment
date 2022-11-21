package com.example.demo.LoginPage.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.LoginPage.model.AuthEntity;
import com.example.demo.LoginPage.service.LoginPageService;
import com.example.demo.LoginPage.utils.LoginCookieUtil;
import com.example.demo.LoginPage.utils.LoginJwtUtil;


@RestController
public class LoginController {
	@Autowired
	ModelAndView model;
	@Autowired
	LoginPageService loginService;
    @Value("${app.baseUrl}")
    private String BASE_URL;
	@Autowired
	LoginJwtUtil jwtUtil;
	@Autowired
	LoginCookieUtil cookieUtil;	
    
	@GetMapping("/")
	public ModelAndView generateOtpPage(HttpServletRequest request) {
		model.setViewName(loginService.generateOtpRedirect(request));
		return model;
	}
	
	@PostMapping("/auth")
	public ModelAndView generateOtp(@ModelAttribute("auth") AuthEntity auth) {
		model.setViewName("redirect:"+BASE_URL+"auth/verify");
		model.addObject("identifier", auth.getIdentifier());
		loginService.handleGenerateOtp(auth);
		return model;
	}
	
	@GetMapping("/auth/verify")
	public ModelAndView authScreen() {
		model.setViewName("loginPage/signIn");
		return model;
	}
	
	@PostMapping("/auth/verify")
	public ModelAndView signinPage(HttpServletResponse response, HttpServletRequest request, @ModelAttribute("auth") AuthEntity auth) {
		Map<String, String> res = loginService.authenticateUser(auth);
		
		if(res.get("statusCode").equals("200")) {
			String userId = loginService.findOrCreateUser(auth);
			String userToken = jwtUtil.generateUserAccessToken(userId, auth.getIdentifier());
			cookieUtil.createUserCookie(response, userToken);
		    
			model.setViewName("redirect:"+BASE_URL+"users/"+userId);
			model.addObject("msg", "");
		}
		else {
			model.setViewName(loginService.signInStatusCodeRedirect(res));
			model.addObject("msg", res.get("msg"));
		}
		
		return model;
	}
	
}



