package com.example.demo.UserDetails.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.WebUtils;

import com.example.demo.UserDetails.model.RolesEntity;
import com.example.demo.UserDetails.model.RolesEnum;
import com.example.demo.UserDetails.model.UserEntity;
import com.example.demo.UserDetails.repository.RolesRepository;
import com.example.demo.UserDetails.repository.UserRepository;
import com.example.demo.UserDetails.utils.UserCookieUtil;
import com.example.demo.UserDetails.utils.UserJwtUtil;
import com.example.demo.UserDetails.validation.UserValidation;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RolesRepository rolesRepo;
	
	@Autowired
	private UserValidation validator;
	
	@Autowired
	UserCookieUtil cookieUtil;
    @Value("${app.baseUrl}")
    private String BASE_URL;
    @Autowired
	UserJwtUtil jwtUtil;
	
	public Map<String, String> getUser(String userId) {
		UserEntity user = new UserEntity();
		validator.userIdValidation(userId);
		Map<String, String> userData = new HashMap<>();
		user = userRepo.findById(Long.parseLong(userId)).get();

		userData.put("userId", user.getId().toString());
		userData.put("firstName", user.getFirstName());
		userData.put("lastName", user.getLastName());
		userData.put("email", user.getEmail());
		userData.put("phone", user.getPhone());
		userData.put("houseDetails", user.getHouseDetails());
		userData.put("locality", user.getLocality());
		userData.put("country", user.getCountry());
		userData.put("pincode", user.getPincode());
		
		return userData;
	}
	
	public String createUser(String identifier, String medium) {
		UserEntity user = new UserEntity();
		RolesEntity rolesEntity = new RolesEntity();
		if(medium.equals("PHONE")) {
			validator.phoneValidation(identifier);
			user.setPhone(identifier);			
		}
		if(medium.equals("EMAIL")) {
			validator.emailValidation(identifier);
			user.setEmail(identifier);			
		}
		
		rolesEntity.setName(RolesEnum.valueOf("USER"));
		if(rolesRepo.findByName(RolesEnum.valueOf("USER"))==null) {
			rolesRepo.save(rolesEntity);			
		}
		else {
			rolesEntity = rolesRepo.findByName(RolesEnum.valueOf("USER"));
		}
		
		Set<RolesEntity> rolesEntitySet = new HashSet<RolesEntity>();
		rolesEntitySet.add(rolesEntity);
		
		user.setRole(rolesEntitySet);
		
		userRepo.save(user);
		return identifier;
	}
	
	public String patchUser(String userId, UserEntity newUser) {
		validator.userIdValidation(userId);
		UserEntity oldUser = userRepo.findById(Long.parseLong(userId)).get();
		validator.nameValidation(newUser.getFirstName());
		validator.nameValidation(newUser.getLastName());
		validator.emailValidation(newUser.getEmail());
		validator.addressValidation(newUser.getHouseDetails(), newUser.getLocality(), newUser.getCountry(), newUser.getPincode());
		
		oldUser.setFirstName(newUser.getFirstName());
		oldUser.setLastName(newUser.getLastName());
		oldUser.setEmail(newUser.getEmail());
		oldUser.setHouseDetails(newUser.getHouseDetails());
		oldUser.setLocality(newUser.getLocality());
		oldUser.setCountry(newUser.getCountry());
		oldUser.setPincode(newUser.getPincode());
		
		userRepo.save(oldUser);
		
		return oldUser.getId().toString();
	}
	
	public String findOrCreate(String identifier, String medium) {
		UserEntity user = new UserEntity();
		if(medium.equals("PHONE")) {
			user = userRepo.findByPhone(identifier);
			if(user!=null) {
				return user.getId().toString();				
			}
		}
		if(medium.equals("EMAIL")) {
			user = userRepo.findByEmail(identifier);
			if(user!=null) {
				return user.getId().toString();				
			}
		}
		
		createUser(identifier, medium);	
		return findOrCreate(identifier, medium);
	}
	
	public String authorizeUserGet(HttpServletResponse response, HttpServletRequest request, String userId) {
		String token = cookieUtil.readCookie(request);
		if(token.equals("Not found!")) {
			return "redirect:"+BASE_URL;
		}
		else {
			Boolean authorized = jwtUtil.validateAccessToken(token);
			if(authorized) {
				Set<RolesEntity> roles = new HashSet<>();
				roles = userRepo.findById(Long.parseLong(userId)).get().getRole();
				String rolesString = "";
				for(RolesEntity role:roles) {
					rolesString+=role.getName().toString();
					rolesString+=", ";	
				}
				if(jwtUtil.getUserId(token).equals(userId) || rolesString.contains("ADMIN")) {
					return "userDetails/userDetails";
				}
				else {
					return "redirect:"+BASE_URL+"users/"+jwtUtil.getUserId(token);					
				}
			}
			else {
				cookieUtil.deleteCookie(response);
				return "redirect:"+BASE_URL;
			}
		}
	}
	
	public String authorizeUserPost(HttpServletResponse response, HttpServletRequest request, String userId, UserEntity user) {
		String token = cookieUtil.readCookie(request);
		if(token.equals("Not found!")) {
			return "redirect:"+BASE_URL;
		}
		else {
			Boolean authorized = jwtUtil.validateAccessToken(token);
			if(authorized) {
				Set<RolesEntity> roles = new HashSet<>();
				roles = userRepo.findById(Long.parseLong(userId)).get().getRole();
				String rolesString = "";
				for(RolesEntity role:roles) {
					rolesString+=role.getName().toString();
					rolesString+=", ";	
				}
				if(jwtUtil.getUserId(token).equals(userId) || rolesString.contains("ADMIN")) {
					patchUser(userId, user);
					return "redirect:"+userId;
				}
				else {
					return "redirect:"+BASE_URL+"users/"+jwtUtil.getUserId(token);					
				}
			}
			else {
				cookieUtil.deleteCookie(response);
				return "redirect:"+BASE_URL;
			}
		}
	}
	
	public String logoutUser(HttpServletResponse response) {
		cookieUtil.deleteCookie(response);
		return ("redirect:"+BASE_URL);
	}
	
}
