package com.myy803.socialbookstore.services.strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.myy803.socialbookstore.domainmodel.Book;
import com.myy803.socialbookstore.domainmodel.BookAuthor;
import com.myy803.socialbookstore.formsdata.SearchFormData;
import com.myy803.socialbookstore.mappers.BookMapper;

public class ApproximateSearchStrategy extends TemplateSearchStrategy {

	@Override
	public List<Book> makeInitialListOfBooks(SearchFormData searchFormData, BookMapper bookMapper) {
		String[] titles = searchFormData.getTitles().split(",");	
		List<Book> books = bookMapper.findAll();
		
		List<Book> results = new ArrayList<Book>();
		for (Book book: books) {
			for (String title: titles) {
				if (book.getTitle().contains(title)) { 
					results.add(book);
				}
			}
		}
		
		return results;
	}

	@Override
	public boolean checkIfAuthorsMatch(SearchFormData searchFormData, Book book) {
		String [] searchAuthorsTemp = searchFormData.getAuthors().split(",");
		List<String> searchAuthors = Arrays.asList(searchAuthorsTemp);
		
		for (BookAuthor author: book.getBookAuthors()) {
			if (!searchAuthors.contains(author.getName())) {
				return false;
			}
		}
				
		return true;
	}

}
