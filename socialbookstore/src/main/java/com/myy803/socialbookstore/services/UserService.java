package com.myy803.socialbookstore.services;

import org.springframework.stereotype.Service;

import com.myy803.socialbookstore.domainmodel.User;

@Service
public interface UserService {
	
	public void saveUser(User user);
	
	public boolean isUserPresent(User user);
	
	public User findById(int id);
}
