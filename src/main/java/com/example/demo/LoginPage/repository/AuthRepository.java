package com.example.demo.LoginPage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.LoginPage.model.AuthEntity;



@Repository
public interface AuthRepository  extends JpaRepository<AuthEntity, Long>  {
	AuthEntity findByIdentifier(String identifier);
}
