package com.myy803.socialbookstore.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myy803.socialbookstore.domainmodel.Book;
import com.myy803.socialbookstore.domainmodel.BookAuthor;
import com.myy803.socialbookstore.domainmodel.BookCategory;
import com.myy803.socialbookstore.domainmodel.UserProfile;
import com.myy803.socialbookstore.formsdata.BookFormData;
import com.myy803.socialbookstore.formsdata.SearchFormData;
import com.myy803.socialbookstore.formsdata.UserProfileFormData;
import com.myy803.socialbookstore.mappers.BookAuthorMapper;
import com.myy803.socialbookstore.mappers.BookCategoryMapper;
import com.myy803.socialbookstore.mappers.BookMapper;
import com.myy803.socialbookstore.mappers.UserProfileMapper;
import com.myy803.socialbookstore.services.strategies.SearchFactory;
import com.myy803.socialbookstore.services.strategies.SearchStrategy;

@Service
public class UserProfileServiceImpl implements UserProfileService {
	
	@Autowired
	private UserProfileMapper userProfileMapper;
	
	@Autowired
	private BookAuthorMapper bookAuthorMapper;
	
	@Autowired
	private BookCategoryMapper bookCategoryMapper;
	
	@Autowired
	private BookMapper bookMapper;
	
	@Autowired
	private SearchFactory searchFactory;
		
	@Override
	public UserProfileFormData retrieveProfile(String username) {
		UserProfile user = userProfileMapper.findByUsername(username);
		
		UserProfileFormData formData;
		if (user != null) {
			formData = convertToUserProfileFormData(user);
		} else { 
			formData = new UserProfileFormData();
			formData.setUsername(username);
		}
		return formData;
	}

	@Override
	public void save(UserProfileFormData userProfileFormData) {
		UserProfile user = convertToUserProfile(userProfileFormData);
		userProfileMapper.save(user);
	}

	@Override
	public List<BookFormData> retrieveBookOffers(String username) {
		UserProfile userProfile = userProfileMapper.findByUsername(username);
		List<Book> books = userProfile.getBookOffers();
		
		List<BookFormData> booksFormData = new ArrayList<BookFormData>();
		for (Book book: books) {
			booksFormData.add(convertToBookFormData(book));
		}
	
		return booksFormData;
	}

	@Override
	public void addBookOffer(String username, BookFormData bookFormData) {
		Book book = new Book();
		book.setTitle(bookFormData.getTitle());
		book.setSummary(bookFormData.getSummary());
		
		List<BookAuthor> bookAuthors = new ArrayList<BookAuthor>();
		String [] givenAuthors = bookFormData.getBookAuthors().split(",");
		
		for (String author: givenAuthors) {
			BookAuthor bookAuthor = bookAuthorMapper.findByName(author);
			if (bookAuthor == null) {
				bookAuthor = new BookAuthor();
				bookAuthor.setName(author);
				bookAuthorMapper.save(bookAuthor);
			}
			
			bookAuthors.add(bookAuthor);
		}
		book.setBookAuthors(bookAuthors);
		
		BookCategory category = bookCategoryMapper.findByName(bookFormData.getBookCategory());
		if (category == null) {
			category = new BookCategory();
			category.setName(bookFormData.getBookCategory());
			bookCategoryMapper.save(category);
		}
		
		book.setBookCategory(category);
		UserProfile userProfile = userProfileMapper.findByUsername(username);
		userProfile.addBookOffers(book);
		
		bookMapper.save(book);
		userProfileMapper.save(userProfile);
	}

	@Override
	public List<BookFormData> searchBooks(SearchFormData searchFormData) {
		String type = searchFormData.getType();
		SearchStrategy strategy = searchFactory.getStrategy(type);
		
		List<BookFormData> results = strategy.search(searchFormData, bookMapper);
		
		return results;
	}

	@Override
	public void requestBook(int bookid, String username) {
		Book book = bookMapper.findById(bookid).get();
		UserProfile user = userProfileMapper.findByUsername(username);
		
		if (!user.getRequestedBooks().contains(book))
			user.addRequestedBook(book);
		userProfileMapper.save(user);
	}
 
	@Override
	public List<BookFormData> retrieveBookRequests(String username) {
		UserProfile user = userProfileMapper.findByUsername(username);
		List<Book> requestedBooks = user.getRequestedBooks();
		
		List<BookFormData> booksFormData = new ArrayList<BookFormData>();
		for (Book book: requestedBooks) {
			booksFormData.add(convertToBookFormData(book));
		}
		
		return booksFormData;
	}

	@Override
	public List<UserProfileFormData> retrieveRequestingUsers(int bookid) {
		Book book = bookMapper.findById(bookid).get();
		List<UserProfile> requestingUsers = book.getRequestingUsers();
		
		List<UserProfileFormData> users = new ArrayList<UserProfileFormData>();
		for (UserProfile user: requestingUsers) {
			users.add(convertToUserProfileFormData(user));
		}
		return users;
	}

	@Override
	public void deleteBookOffer(String username, int bookid) {
		Book book = bookMapper.findById(bookid).orElse(null);
		List<UserProfile> requestingUsers = book.getRequestingUsers();
		
		if (book != null) {
			for (UserProfile user: requestingUsers) {
				user.getRequestedBooks().remove(book);
				userProfileMapper.save(user);
			}
			
			book.getRequestingUsers().clear();
			
			UserProfile userProfile = userProfileMapper.findByUsername(username);
			userProfile.getBookOffers().remove(book); 
			userProfileMapper.save(userProfile);
			
			bookMapper.delete(book);
		}
	}

	@Override
	public void deleteBookRequest(String username, int bookid) {
		UserProfile user = userProfileMapper.findByUsername(username);
		Book book = bookMapper.findById(bookid).orElse(null);
		
		if (book != null) {
			user.getRequestedBooks().remove(book);
			book.getRequestingUsers().remove(user);
			bookMapper.save(book);
		}
	}
	
	@Override
	public void acceptBookRequest(String username, int bookid) {
		Book book = bookMapper.findById(bookid).get();
		
		book.setAcceptedUsername(username);
		bookMapper.save(book);
	}
	
	@Override
	public BookFormData findBookById(int bookid) {
		return convertToBookFormData(bookMapper.findById(bookid).get());
	}
	
	public UserProfile convertToUserProfile(UserProfileFormData formData) {
		UserProfile profile = userProfileMapper.findByUsername(formData.getUsername());
		
		if (profile == null) {
			profile = new UserProfile();
		}
		
		profile.setUsername(formData.getUsername());
		profile.setFullName(formData.getFullName());
		profile.setAddress(formData.getAddress());
		profile.setAge(formData.getAge());
		profile.setPhoneNumber(formData.getPhoneNumber());

		List<BookAuthor> authors = new ArrayList<BookAuthor>();
		String [] favAuthors = formData.getFavoriteBookAuthors().split(",");
		for (String author : favAuthors) {
			BookAuthor bookAuthor = bookAuthorMapper.findByName(author);
			if (bookAuthor == null)   {
				bookAuthor = new BookAuthor();
				bookAuthor.setName(author);
				bookAuthorMapper.save(bookAuthor);
			}
			
			authors.add(bookAuthor);
		}
		profile.setFavoriteBookAuthors(authors);
		
		List<BookCategory> categories = new ArrayList<BookCategory>();
		List<String> favCategories = formData.getFavoriteBookCategories();
		for (String category: favCategories) {
			BookCategory bookCategory = bookCategoryMapper.findByName(category);
			if (bookCategory == null) {
				bookCategory = new BookCategory();
				bookCategory.setName(category);
				bookCategoryMapper.save(bookCategory);
			}
			
			categories.add(bookCategory);
		}
		profile.setFavoriteBookCategories(categories);
						
		return profile;
	} 
	
	public static UserProfileFormData convertToUserProfileFormData(UserProfile user) {
		UserProfileFormData formData = new UserProfileFormData();
		formData.setId(user.getUser_profile_id());
		formData.setUsername(user.getUsername());
		formData.setFullName(user.getFullName());
		formData.setAddress(user.getAddress());
		formData.setAge(user.getAge());
		formData.setPhoneNumber(user.getPhoneNumber());
		
		String authorsToString = "";
		for (int i=0; i<user.getFavoriteBookAuthors().size(); i++) {
			if (i != user.getFavoriteBookAuthors().size()-1)
				authorsToString += user.getFavoriteBookAuthors().get(i).getName() + ",";
			else
				authorsToString += user.getFavoriteBookAuthors().get(i).getName();
		}
		formData.setFavoriteBookAuthors(authorsToString);
		
		List<String> categories = new ArrayList<String>();
		List<BookCategory> favCategories = user.getFavoriteBookCategories();
		
		for (BookCategory category: favCategories) {
			categories.add(category.getName());
		}
		formData.setFavoriteBookCategories(categories);
				
		return formData;
	}
	
	public BookFormData convertToBookFormData(Book book) {
		BookFormData tempBook = new BookFormData();
		tempBook.setId(book.getBook_id());
		tempBook.setTitle(book.getTitle());
		tempBook.setAcceptedUsername(book.getAcceptedUsername());
		tempBook.setSummary(book.getSummary());
		tempBook.setBookCategory(book.getBookCategory().getName());
		
		String authorsString = "";		
		for (int i=0; i<book.getBookAuthors().size(); i++) {
			if (i != book.getBookAuthors().size()-1)
				authorsString += book.getBookAuthors().get(i).getName() + ",";
			else
				authorsString += book.getBookAuthors().get(i).getName();
		}
		tempBook.setBookAuthors(authorsString);
				
		return tempBook;
	}
}
