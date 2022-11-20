package com.example.demo;

import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
   
    @Value("${app.baseUrl}")
    private String BASE_URL;
	
    @ExceptionHandler(value = NoSuchElementException.class)
    public ModelAndView exception(NoSuchElementException exception, HttpServletRequest req) {
	    ModelAndView model = new ModelAndView("error");
	    model.addObject("error", exception.getMessage());
	    model.addObject("redirect", BASE_URL);
	    return model;
    }
	
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ModelAndView exception(IllegalArgumentException exception, HttpServletRequest req) {
	    ModelAndView model = new ModelAndView("error");
	    model.addObject("error", exception.getMessage());
	    model.addObject("redirect", "/");
	    return model;
    }
}
