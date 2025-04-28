package com.myy803.socialbookstore.mappers;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myy803.socialbookstore.domainmodel.User;

public interface UserMapper extends JpaRepository<User, Integer> {
	public User findByUsername(String username);
}
