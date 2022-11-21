package com.example.demo.UserDetails.validation;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;


@Service
public class UserValidation {
	
	public void nameValidation(String name) {
		if(name!="") {
			if(name.length()<=2) {
				throw new IllegalArgumentException("Invalid Name; Should have atleast 3 characters");
			}
			if(!name.matches("[a-zA-Z]+")) {
				throw new IllegalArgumentException("Name should only contain letters");
			}	
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
	
	public void addressValidation(String houseDetails, String locality, String country, String pincode) {
		if(houseDetails!="" && locality!="" && country!="" && pincode!="") {
			if(!locality.matches("^[a-zA-Z0-9]+$")) {
				throw new IllegalArgumentException("Locality name should only contain letters or numbers");
			}

			if(!country.matches("[a-zA-Z]+")) {
				throw new IllegalArgumentException("Country name must be a valid string");
			}
			
			try {
				final int intPincode = Integer.parseInt(pincode);
				if(intPincode<1000 || intPincode>999999) {
					throw new IllegalArgumentException("Pincode must be a 4-6 digit integer");
				}
			}
			catch(Exception e){
				throw new IllegalArgumentException("Pincode must be a number");
			}
		}
		else {
			if(houseDetails!="" || locality!="" || country!="" || pincode!="") {
				throw new IllegalArgumentException("Enter the complete address to save");
			}
		}
	}
	
	public void userIdValidation(String userId) {
		try {
			Long.parseLong(userId);
		}
		catch(Exception e){
			throw new IllegalArgumentException("UserID must be a number");
		}
	}
	
	public void phoneValidation(String phone) {
		String phoneRegex = "^\\d{10}$";
		Pattern pattern = Pattern.compile(phoneRegex);

		if(!pattern.matcher(phone).matches()) {
			throw new NoSuchElementException("Invalid Phone");
		}
	}
	
}
