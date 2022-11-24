package com.example.demo.LoginPage.controller;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public ModelAndView generateOtpPage(HttpServletRequest request, RedirectAttributes ra) {
		model.clear();
		model.setViewName(loginService.generateOtpRedirect(request));
		return model;
	}
	
	@PostMapping("/auth")
	public ModelAndView generateOtp(@ModelAttribute("auth") AuthEntity auth, RedirectAttributes ra) {
		model.clear();
		model.setViewName("redirect:"+BASE_URL+"auth/verify?identifier="+auth.getIdentifier());
		loginService.handleGenerateOtp(auth);
		return model;
	}
	
	@GetMapping("/auth/verify")
	public ModelAndView authScreen(@RequestParam("medium") Optional<String> medium, @RequestParam("identifier") String identifier, RedirectAttributes ra) {
		model.setViewName("loginPage/signIn");
		Map<String, String> data = loginService.authScreenData(medium, identifier);
		model.addObject("data", data);
		return model;
	}
	
	@PostMapping("/auth/verify")
	public ModelAndView signinPage(HttpServletResponse response, HttpServletRequest request, @ModelAttribute("auth") AuthEntity auth, RedirectAttributes ra) {
		model.clear();
		Map<String, String> res = loginService.authenticateUser(auth);
		String userToken = cookieUtil.readUserCookie(request);
		Map<String, String> data = loginService.userLoginData(response, userToken, auth, res);
		model.setViewName(data.get("view"));
		model.addObject("msg", data.get("msg"));
		return model;
	}
	
}



