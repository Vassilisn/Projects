package com.myy803.socialbookstore.mappers;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myy803.socialbookstore.domainmodel.BookCategory;

public interface BookCategoryMapper extends JpaRepository<BookCategory, Integer>{
	public BookCategory findByName(String name);
}
