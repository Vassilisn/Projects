package com.myy803.socialbookstore.mappers;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myy803.socialbookstore.domainmodel.UserProfile;

public interface UserProfileMapper extends JpaRepository<UserProfile, Integer>{
	public UserProfile findByUsername(String username);
} 
