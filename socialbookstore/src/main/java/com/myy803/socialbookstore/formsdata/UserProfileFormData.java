package com.myy803.socialbookstore.formsdata;

import java.util.ArrayList;
import java.util.List;

public class UserProfileFormData {
	
	private int id;
	private String username;
	private String fullName;
	private String address;
	private int age;
	private String phoneNumber;
	private String favoriteBookAuthors;
	private List<String> favoriteBookCategories = new ArrayList<String>();
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFavoriteBookAuthors() {
		return favoriteBookAuthors;
	}

	public void setFavoriteBookAuthors(String favoriteBookAuthors) {
		this.favoriteBookAuthors = favoriteBookAuthors;
	}

	public List<String> getFavoriteBookCategories() {
		return favoriteBookCategories;
	}

	public void setFavoriteBookCategories(List<String> favoriteBookCategories) {
		this.favoriteBookCategories = favoriteBookCategories;
	}
}
