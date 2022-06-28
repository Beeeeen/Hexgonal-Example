package com.systex.msg.book.iface.event;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.systex.msg.base.iface.event.BaseEventHandler;
import com.systex.msg.book.domain.book.command.ReleaseBookCommand;
import com.systex.msg.book.domain.book.outbound.BookCreatedEvent;
import com.systex.msg.book.domain.book.outbound.BookCreatedEventData;
import com.systex.msg.book.domain.book.outbound.BookUpdatedEvent;
import com.systex.msg.book.domain.book.outbound.BookUpdatedEventData;
import com.systex.msg.book.infra.broker.BookEventSource;
import com.systex.msg.book.service.BookCommandService;
import com.systex.msg.util.BaseDataTransformer;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@EnableBinding(BookEventSource.class)
@AllArgsConstructor
public class BookEventHandler extends BaseEventHandler {

	BookCommandService service;

	@StreamListener(target = BookEventSource.Topic.BOOK_CREATING_SUB)
	public void receiveEvent(BookCreatedEvent event) {
		BookCreatedEventData eventData = event.getData();
		log.info("[EVENT] receive event, data: {}", eventData);

		// 防腐處理
		ReleaseBookCommand command = BaseDataTransformer.transformDTO(eventData, ReleaseBookCommand.class);

		// 冪等機制，防止重覆消費所帶來的副作用
		this.setEventIdempotent(command, event);
		
		// 呼叫 Application Service
		service.release(command);
	}

	@StreamListener(target = BookEventSource.Topic.BOOK_UPDATING_SUB)
	public void receiveEvent(BookUpdatedEvent event) {
		BookUpdatedEventData eventData = event.getData();
		log.info("[EVENT] receive event, data: {}", eventData);

		// 防腐處理
		ReleaseBookCommand command = BaseDataTransformer.transformDTO(eventData, ReleaseBookCommand.class);

		// 冪等機制，防止重覆消費所帶來的副作用
		this.setEventIdempotent(command, event);
		
		// 呼叫 Application Service
		service.release(command);
	}

}
