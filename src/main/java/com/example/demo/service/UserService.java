package com.example.demo.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.RolesEntity;
import com.example.demo.model.RolesEnum;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.RolesRepository;
import com.example.demo.repository.UserRepository;

import com.example.demo.validation.PersonalDetailsValidation;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RolesRepository rolesRepo;
	
	@Autowired
	private PersonalDetailsValidation validator;
	
	UserEntity user = new UserEntity();
	RolesEntity rolesEntity = new RolesEntity();
	
	public Map<String, String> getUser(String userId) {
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
	
	
	public String createUser(String phone) {
		user.setPhone(phone);
		rolesEntity.setName(RolesEnum.valueOf("USER"));
		rolesRepo.save(rolesEntity);
		Set<RolesEntity> rolesEntitySet = new HashSet<RolesEntity>();
		rolesEntitySet.add(rolesEntity);
		user.setRole(rolesEntitySet);
		userRepo.save(user);
		return phone;
	}
	
	public Long getUserByPhone(String phone) {
		return userRepo.findByPhone(phone).getId();
	}
	
	public Long patchUser(String userId, UserEntity newUser) {
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
		
		return oldUser.getId();
	}	
}
