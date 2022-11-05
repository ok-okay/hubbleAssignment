package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.UserEntity;
import com.example.demo.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	ModelAndView model;

	@GetMapping("/users/{userId}")
	public ModelAndView getUser(@PathVariable String userId) {
		model.setViewName("userDetails");
		Map<String, String> userData = userService.getUser(userId);
		model.addObject("userData", userData);
		return model;
	}
	
	@PostMapping("/users")
	public ModelAndView createUser(@RequestParam("phone") String phone){
		userService.handleCreateUser(phone);
		long userId = userService.getUserByPhone(phone);
		model.setViewName("redirect:users/"+userId);
		return model;
	}
	
	@PostMapping("users/{userId}")
	public ModelAndView patchUser(@PathVariable String userId, @ModelAttribute("user") UserEntity user){
		userService.patchUser(userId, user);
		model.setViewName("redirect:"+userId);
		return model;
	}
	
}
