package com.example.demo.UserDetails.controller;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.UserDetails.model.UserEntity;
import com.example.demo.UserDetails.service.UserService;
import com.example.demo.UserDetails.utils.UserCookieUtil;
import com.example.demo.UserDetails.utils.UserJwtUtil;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	ModelAndView model;
	@Autowired
	UserCookieUtil cookieUtil;
	@Autowired
	UserJwtUtil jwtUtil;
	@Value("${app.baseUrl}")
    private String BASE_URL;
	
	@PostMapping("/users")
	public String findOrCreate(@RequestBody Map<String, String> data){
		String userId = userService.findOrCreate(data.get("identifier"), data.get("medium"));
		return userId;
	}
	
	@GetMapping("/users/{userId}")
	public ModelAndView getUser(HttpServletResponse response, HttpServletRequest request, @PathVariable String userId, @RequestParam("updateToken") Optional<String> updateToken, RedirectAttributes ra) {
		String redirectUrl = "redirect:"+BASE_URL;
		if(!updateToken.isEmpty()) {
			userService.updateIdentifier(response, updateToken.get());			
		}
		else {
			redirectUrl = userService.authorizeUserGet(response, request, userId);			
		}
		model.setViewName(redirectUrl);
		
		if(!redirectUrl.contains("redirect")) {
			Map<String, String> userData = userService.getUser(userId);
			model.addObject("userData", userData);
		}
		return model;
	}
	
	@PostMapping("users/{userId}")
	public ModelAndView patchUser(HttpServletResponse response, HttpServletRequest request, @PathVariable String userId, @ModelAttribute("user") UserEntity user, RedirectAttributes ra){
		model.clear();
		String redirectUrl = userService.authorizeUserPost(response, request, userId, user);
		model.setViewName(redirectUrl);
		return model;
	}
	
	@PostMapping("users/logout")
	public ModelAndView logoutUser(HttpServletResponse response){
		model.clear();
		String redirectUrl = userService.logoutUser(response);
		model.setViewName(redirectUrl);
		return model;
	}
	
}
