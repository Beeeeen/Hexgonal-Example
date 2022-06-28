package com.systex.msg.book.domain.book.outbound;

import com.systex.msg.base.domain.outbound.BaseEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class BookUpdatedEvent extends BaseEvent {

	@Getter
	private BookUpdatedEventData data;

}
