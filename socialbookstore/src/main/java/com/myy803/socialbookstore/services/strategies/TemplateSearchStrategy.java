package com.myy803.socialbookstore.services.strategies;

import java.util.ArrayList;
import java.util.List;

import com.myy803.socialbookstore.domainmodel.Book;
import com.myy803.socialbookstore.formsdata.BookFormData;
import com.myy803.socialbookstore.formsdata.SearchFormData;
import com.myy803.socialbookstore.mappers.BookMapper;

public abstract class TemplateSearchStrategy implements SearchStrategy {
	
	@Override
	public List<BookFormData> search(SearchFormData searchFormData, BookMapper bookMapper) {
		List<Book> books = makeInitialListOfBooks(searchFormData, bookMapper);
		
		List<BookFormData> results = new ArrayList<BookFormData>();
		for (Book book: books) {
			if (checkIfAuthorsMatch(searchFormData, book)) {
				BookFormData tempBook = convertToBookFormData(book);
				
				results.add(tempBook);
			}
			
		}
		
		return results;
	}
	
	public abstract List<Book> makeInitialListOfBooks(SearchFormData searchFormData, BookMapper bookMapper);
	
	public abstract boolean checkIfAuthorsMatch(SearchFormData searchFormData, Book book);
	
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

