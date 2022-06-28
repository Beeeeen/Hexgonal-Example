package com.systex.msg.book.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.systex.msg.base.domain.AggregateProxyFactory;
import com.systex.msg.base.domain.eventlog.aggregate.EventLog;
import com.systex.msg.base.domain.outbound.BaseEvent;
import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.base.infra.repository.EventLogRepository;
import com.systex.msg.base.service.BaseApplicationService;
import com.systex.msg.book.domain.book.aggregate.Book;
import com.systex.msg.book.domain.book.command.CreateBookCommand;
import com.systex.msg.book.domain.book.command.ReleaseBookCommand;
import com.systex.msg.book.domain.book.command.RenameBookCommand;
import com.systex.msg.book.domain.book.command.UpdateBookCommand;
import com.systex.msg.book.domain.service.BookService;
import com.systex.msg.book.infra.repository.BookRepository;
import com.systex.msg.book.service.outbound.ExternalBookService;
import com.systex.msg.config.ContextHolder;
import com.systex.msg.exception.NotFoundException;
import com.systex.msg.exception.ValidateFailedException;
import com.systex.msg.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

/**
 * Application Service class for the Commands
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
@AllArgsConstructor
public class BookCommandService extends BaseApplicationService {

	private BookRepository bookRepository;
	private EventLogRepository eventLogRepository;
	private BookService bookService;
	private ExternalBookService externalService;

	/**
	 * Service Command method to create
	 * 
	 * @param command
	 * @return
	 * @throws ValidateFailedException 
	 */
	public Book create(CreateBookCommand command) throws ValidateFailedException {
		
		// 叫用 Domain Service 進行領域檢核
		bookService.checkCouponUsed(command.getCoupon());

		// 叫用 Domain Service 取得組裝資訊
		UUID couponNo = bookService.getCouponNo(command.getCoupon());
		command.setCouponNo(couponNo);
		
		// 叫用 Command Handler
		Book book = new Book();
		Book proxy = AggregateProxyFactory.getProxyInstance(Book.class, book);
		proxy.create(command);
		
		// 儲存 Aggregate
		bookRepository.save(book);
		
		// 寫入 EventLog（當有 Next Event 需要發佈時）
		BaseEvent event = ContextHolder.getEvent();
		EventLog eventLog = new EventLog(event.getEventLogUuid(), BaseDataTransformer.transformEvent(event));
		eventLogRepository.save(eventLog);

		return book;
	}

	/**
	 * Service Command method to update
	 * 
	 * @return Book
	 */
	public Book update(UpdateBookCommand command) {

		// 取得本次交易 Aggregate
		Optional<Book> opt = bookRepository.findById(new UUID(command.getBookId()));
		if (!opt.isPresent())
			throw new NotFoundException(String.format("book not found (%s)", command.getBookId()));
		
		// 叫用 Command Handler
		Book book = opt.get();
		Book proxy = AggregateProxyFactory.getProxyInstance(Book.class, book);
		proxy.update(command);

		// 儲存 Aggregate
		bookRepository.save(book);

		// 寫入 EventLog（當有 Next Event 需要發佈時）
		BaseEvent event = ContextHolder.getEvent();
		EventLog eventLog = new EventLog(event.getEventLogUuid(), BaseDataTransformer.transformEvent(event));
		eventLogRepository.save(eventLog);

		return book;
	}

	/**
	 * Service Command method to patch
	 * 
	 * @return Book
	 */
	public Book release(ReleaseBookCommand command) {
		
		// 冪等機制，防止重覆消費所帶來的副作用
		if (!this.checkEventIdempotent(command))
			return null;

		// 取得本次交易 Aggregate
		Optional<Book> opt = bookRepository.findById(new UUID(command.getBookId()));
		if (!opt.isPresent())
			throw new NotFoundException(String.format("book not found (%s)", command.getBookId()));

		// 叫用 Command Handler
		Book book = opt.get();
		Book proxy = AggregateProxyFactory.getProxyInstance(Book.class, book);
		proxy.release(command);
		
		// 儲存 Aggregate
		bookRepository.save(book);

		// 冪等機制，防止重覆消費所帶來的副作用
		this.insertEventIdempotent(command);
		
		return book;
	}

	/**
	 * Service Command method to rename
	 * 
	 * @return Book
	 */
	public Book rename(RenameBookCommand command) {

		// 取得本次交易 Aggregate
		Optional<Book> opt = bookRepository.findById(new UUID(command.getBookId()));
		if (!opt.isPresent())
			throw new NotFoundException(String.format("book not found (%s)", command.getBookId()));

		// 呼叫 Outbound Service 取得組裝資訊
		String name = externalService.queryBookName(command.getExternalBookId());
		command.setName(name);

		// 叫用 Command Handler
		Book book = opt.get();
		Book proxy = AggregateProxyFactory.getProxyInstance(Book.class, book);
		proxy.rename(command);

		// 儲存 Aggregate
		bookRepository.save(book);

		return book;
	}

}
