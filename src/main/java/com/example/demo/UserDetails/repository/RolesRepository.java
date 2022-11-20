package com.example.demo.UserDetails.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.UserDetails.model.RolesEntity;
import com.example.demo.UserDetails.model.RolesEnum;

@Repository
public interface RolesRepository extends JpaRepository<RolesEntity, Long>{	
	RolesEntity findByName(RolesEnum role);
}
