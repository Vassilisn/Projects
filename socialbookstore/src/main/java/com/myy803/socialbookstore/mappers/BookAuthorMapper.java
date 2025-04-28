package com.myy803.socialbookstore.mappers;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myy803.socialbookstore.domainmodel.BookAuthor;

public interface BookAuthorMapper extends JpaRepository<BookAuthor, Integer>{
	public BookAuthor findByName(String name);
}
