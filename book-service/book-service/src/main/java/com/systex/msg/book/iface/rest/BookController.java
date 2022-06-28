package com.systex.msg.book.iface.rest;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.systex.msg.base.iface.rest.BaseController;
import com.systex.msg.base.iface.rest.dto.UUIDResource;
import com.systex.msg.book.domain.book.aggregate.Book;
import com.systex.msg.book.domain.book.command.CreateBookCommand;
import com.systex.msg.book.domain.book.command.QueryBookCommand;
import com.systex.msg.book.domain.book.command.RenameBookCommand;
import com.systex.msg.book.domain.book.command.UpdateBookCommand;
import com.systex.msg.book.iface.rest.dto.BookResource;
import com.systex.msg.book.iface.rest.dto.CreateBookResource;
import com.systex.msg.book.iface.rest.dto.UpdateBookResource;
import com.systex.msg.book.service.BookCommandService;
import com.systex.msg.book.service.BookQueryService;
import com.systex.msg.util.BaseDataTransformer;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RequestMapping("/api/v1/book")
@RestController
@AllArgsConstructor
@Tag(name = "Book API", description = "Book API")
public class BookController extends BaseController {

	BookCommandService commandService; // Application Service Dependency

	BookQueryService queryService; // Application Service Dependency

	static {
		BaseDataTransformer.addConverter(BookResource.getConverter(), Book.class, BookResource.class);
	}

	/**
	 * Create a book
	 * 
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public UUIDResource create(@Valid @RequestBody CreateBookResource resource) throws Exception {

		// DTO 防腐處理 (DTO > Command)
		CreateBookCommand command = BaseDataTransformer.transformDTO(resource, CreateBookCommand.class);

		// 呼叫 Application Service
		Book book = commandService.create(command);

		// DTO 防腐處理 (Domain > DTO)，並回傳
		return BaseDataTransformer.transformAggregate(book.getUuid(), UUIDResource.class);
	}

	/**
	 * Update a book
	 * 
	 * @param bookId
	 * @param book
	 * @return
	 */
	@PutMapping("/{bookId}")
	public UUIDResource update(@PathVariable String bookId, @Valid @RequestBody UpdateBookResource resource) {

		// DTO 防腐處理 (DTO > Command)
		UpdateBookCommand command = BaseDataTransformer.transformDTO(resource, UpdateBookCommand.class);
		command.setBookId(bookId);

		// 呼叫 Application Service
		Book book = commandService.update(command);

		// DTO 防腐處理 (Domain > DTO)，並回傳
		return BaseDataTransformer.transformAggregate(book.getUuid(), UUIDResource.class);
	}

	/**
	 * Rename a book
	 * 
	 * @param bookId
	 * @param externalBookId
	 * @return
	 */
	@PatchMapping("/{bookId}/rename/{externalBookId}")
	public UUIDResource rename(@PathVariable String bookId, @PathVariable String externalBookId) {

		// DTO 防腐處理 (DTO > Command)
		RenameBookCommand command = new RenameBookCommand(bookId, externalBookId);

		// 呼叫 Application Service
		Book book = commandService.rename(command);

		// DTO 防腐處理 (Domain > DTO)，並回傳
		return BaseDataTransformer.transformAggregate(book.getUuid(), UUIDResource.class);
	}

	/**
	 * Query a book
	 * 
	 * @param bookId
	 * @return
	 */
	@GetMapping("/{bookId}")
	public BookResource query(@PathVariable String bookId) {
		QueryBookCommand command = new QueryBookCommand(bookId);
		Book book = queryService.query(command);

		return BaseDataTransformer.transformAggregate(book, BookResource.class);
	}

}
