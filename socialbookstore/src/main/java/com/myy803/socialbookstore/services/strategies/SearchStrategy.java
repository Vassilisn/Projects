package com.myy803.socialbookstore.services.strategies;

import java.util.List;

import com.myy803.socialbookstore.formsdata.BookFormData;
import com.myy803.socialbookstore.formsdata.SearchFormData;
import com.myy803.socialbookstore.mappers.BookMapper;

public interface SearchStrategy {
	public List<BookFormData> search(SearchFormData searchFormData, BookMapper bookMapper);
}
