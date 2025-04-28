package com.myy803.socialbookstore.domainmodel;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int book_id;
	private String title;
	@ManyToMany
	@JoinTable(
	        name = "books_authors",
	        joinColumns = @JoinColumn(name = "book_id"),
	        inverseJoinColumns = @JoinColumn(name = "author_id"))
	private List<BookAuthor> bookAuthors = new ArrayList<BookAuthor>();
	@ManyToOne
	private BookCategory bookCategory;
	@OneToMany
    @JoinTable(name = "requesting_users",
               joinColumns = @JoinColumn(name = "book_id"),
               inverseJoinColumns = @JoinColumn(name = "user_profile_id"))
	private List<UserProfile> requestingUsers = new ArrayList<UserProfile>();
	private String acceptedUsername;
	private String summary;
	
	public int getBook_id() {
		return book_id;
	}
	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	public String getTitle() {
		return title; 
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<BookAuthor> getBookAuthors() {
		return bookAuthors;
	}
	public void setBookAuthors(List<BookAuthor> bookAuthors) {
		this.bookAuthors = bookAuthors;
	}
	public BookCategory getBookCategory() {
		return bookCategory;
	}
	public void setBookCategory(BookCategory bookCategory) {
		this.bookCategory = bookCategory;
	}
	public List<UserProfile> getRequestingUsers() {
		return requestingUsers;
	}
	public void addRequestingUser(UserProfile requestingUser) {
		this.requestingUsers.add(requestingUser);
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
