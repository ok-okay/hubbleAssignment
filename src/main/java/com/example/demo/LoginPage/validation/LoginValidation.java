package com.example.demo.LoginPage.validation;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class LoginValidation {
	public void phoneValidation(String phone) {
		
		String phoneRegex = "^\\d{10}$";
		Pattern pattern = Pattern.compile(phoneRegex);

		if(!pattern.matcher(phone).matches()) {
			throw new NoSuchElementException("Invalid Phone");
		}
	}
	
	public void emailValidation(String email) {
		if(email!="") {
	        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
	                "[a-zA-Z0-9_+&*-]+)*@" +
	                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
	                "A-Z]{2,7}$";
	                  
			Pattern pat = Pattern.compile(emailRegex);
			if(!pat.matcher(email).matches()) {
				throw new IllegalArgumentException("Invalid Email");
			}	
		}
	}
}
