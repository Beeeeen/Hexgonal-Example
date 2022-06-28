package com.systex.msg.book.infra.broker;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Interface depicting all output channels
 */
public interface BookEventSource {

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	final class Topic {
		public static final String BOOK_CREATING_PUB = "book-creating-pub";
		public static final String BOOK_CREATING_SUB = "book-creating-sub";
		public static final String BOOK_UPDATING_PUB = "book-updating-pub";
		public static final String BOOK_UPDATING_SUB = "book-updating-sub";
	}

	@Output(Topic.BOOK_CREATING_PUB)
	MessageChannel bookCreatingPub();

	@Input(Topic.BOOK_CREATING_SUB)
	SubscribableChannel bookCreatingSub();

	@Output(Topic.BOOK_UPDATING_PUB)
	MessageChannel bookUpdatingPub();

	@Input(Topic.BOOK_UPDATING_SUB)
	SubscribableChannel bookUpdatingSub();
}
