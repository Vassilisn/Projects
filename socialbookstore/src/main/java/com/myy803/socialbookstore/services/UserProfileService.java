package com.myy803.socialbookstore.services;
 
import java.util.List;

import com.myy803.socialbookstore.formsdata.BookFormData;
import com.myy803.socialbookstore.formsdata.SearchFormData;
import com.myy803.socialbookstore.formsdata.UserProfileFormData;

public interface UserProfileService {
	
	public UserProfileFormData retrieveProfile(String username);
	
	public void save(UserProfileFormData userProfileFormData);
	
	public List<BookFormData> retrieveBookOffers(String username);
	
	public void addBookOffer(String username, BookFormData bookFormData);
	
	public List<BookFormData> searchBooks(SearchFormData searchFormData);
	
	public void requestBook(int bookid, String username);
	
	public List<BookFormData> retrieveBookRequests(String username);
	
	public List<UserProfileFormData> retrieveRequestingUsers(int bookid);
	
	public void deleteBookOffer(String username, int bookid);
	
	public void deleteBookRequest(String username, int bookid);
	
	public void acceptBookRequest(String username, int bookid);
	
	public BookFormData findBookById(int bookid);
}
