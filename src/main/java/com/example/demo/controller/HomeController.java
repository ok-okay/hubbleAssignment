package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	@Autowired
	ModelAndView model;
	
	@GetMapping("/")
	public ModelAndView welcomePage() {
		model.setViewName("home");
		return model;
	}
}