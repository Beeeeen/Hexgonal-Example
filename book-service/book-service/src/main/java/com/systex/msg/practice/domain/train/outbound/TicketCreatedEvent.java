package com.systex.msg.practice.domain.train.outbound;


import com.systex.msg.base.domain.outbound.BaseEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TicketCreatedEvent extends BaseEvent {
	
	
	@Getter
	private TicketCreatedEventData data;

	

	
}
