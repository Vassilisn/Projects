package com.myy803.socialbookstore.formsdata;

public class BookFormData {
	private int id;
	private String title;
	private String bookAuthors;
	private String bookCategory;
	private String acceptedUsername;
	private String summary;
	
	public int getId() {
		return id;
	} 
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() { 
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBookAuthors() {
		return bookAuthors;
	}
	public void setBookAuthors(String bookAuthors) {
		this.bookAuthors = bookAuthors;
	}
	public String getBookCategory() {
		return bookCategory;
	}
	public void setBookCategory(String bookCategory) {
		this.bookCategory = bookCategory;
	}
	public String getAcceptedUsername() {
		return acceptedUsername;
	}
	public void setAcceptedUsername(String acceptedUsername) {
		this.acceptedUsername = acceptedUsername;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}	
}
