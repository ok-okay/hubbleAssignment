package com.example.demo.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class UserException {
   @ExceptionHandler(value = IllegalArgumentException.class)
   public ModelAndView exception(IllegalArgumentException exception, HttpServletRequest req) {
	   ModelAndView model = new ModelAndView("error");
	   model.addObject("error", exception.getMessage());
	   model.addObject("redirect", req.getRequestURI());
	   return model;
   }
}
