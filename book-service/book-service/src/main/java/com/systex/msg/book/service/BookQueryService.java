package com.systex.msg.book.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.base.service.BaseApplicationService;
import com.systex.msg.book.domain.book.aggregate.Book;
import com.systex.msg.book.domain.book.command.QueryBookCommand;
import com.systex.msg.book.infra.repository.BookRepository;
import com.systex.msg.exception.NotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookQueryService extends BaseApplicationService {

	private BookRepository repository; // Inject Dependencies

	/**
	 * Find a Book
	 * 
	 * @param uuid
	 * @return
	 */
	public Book query(QueryBookCommand command) {
		Optional<Book> book = repository.findById(new UUID(command.getBookId()));
		if (book.isPresent())
			return book.get();
		else
			throw new NotFoundException(String.format("book not found (%s)", command.getBookId()));

	}

}
