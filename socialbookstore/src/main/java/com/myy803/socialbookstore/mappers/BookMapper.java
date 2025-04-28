package com.myy803.socialbookstore.mappers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myy803.socialbookstore.domainmodel.Book;

public interface BookMapper extends JpaRepository<Book, Integer> {
	public List<Book> findByTitle(String title);
	public List<Book> findByTitleContaining(String title);
}
