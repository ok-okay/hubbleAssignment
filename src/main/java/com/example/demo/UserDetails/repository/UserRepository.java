package com.example.demo.UserDetails.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.UserDetails.model.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	UserEntity findByPhone(String phone);
	UserEntity findByEmail(String emai);
	Optional<UserEntity> findById(Long userId);
	Boolean existsByEmail(String email);
}