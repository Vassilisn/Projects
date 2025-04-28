package com.myy803.socialbookstore.domainmodel;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class UserProfile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int user_profile_id;
	
	@Column(unique=true)
	private String username;
	private String fullName;
	private String address;
	private int age;
	private String phoneNumber;
	@ManyToMany
	List<BookAuthor> favoriteBookAuthors;
	@ManyToMany
	List<BookCategory> favoriteBookCategories;
	@OneToMany
	List<Book> bookOffers = new ArrayList<Book>();
	
	@ManyToMany
    @JoinTable( name = "requesting_users",
        		joinColumns = @JoinColumn(name = "user_profile_id"),
        		inverseJoinColumns = @JoinColumn(name = "book_id"))
	List<Book> requestedBooks = new ArrayList<Book>(); 
	
	public int getUser_profile_id() {
		return user_profile_id;
	}
	public void setUser_profile_id(int user_profile_id) {
		this.user_profile_id = user_profile_id;
	}
	public List<Book> getRequestedBooks() {
		return requestedBooks;
	}
	public void setRequestedBooks(List<Book> requestedBooks) {
		this.requestedBooks = requestedBooks;
	}
	public void addRequestedBook(Book requestedBook) {
		this.requestedBooks.add(requestedBook);
	}
	public void removeRequestedBook(Book requestedBook) {
		this.requestedBooks.remove(requestedBook);
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public List<BookAuthor> getFavoriteBookAuthors() {
		return favoriteBookAuthors;
	}
	public void setFavoriteBookAuthors(List<BookAuthor> favoriteBookAuthors) {
		this.favoriteBookAuthors = favoriteBookAuthors;
	}
	public List<BookCategory> getFavoriteBookCategories() {
		return favoriteBookCategories;
	}
	public void setFavoriteBookCategories(List<BookCategory> favoriteBookCategories) {
		this.favoriteBookCategories = favoriteBookCategories;
	}
	public List<Book> getBookOffers() {
		return bookOffers;
	}
	public void setBookOffers(List<Book> bookOffers) {
		this.bookOffers = bookOffers;
	}
	public void addBookOffers(Book bookOffer) {
		this.bookOffers.add(bookOffer);
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
