package com.systex.msg.base.domain.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventLogCreatedEvent {

	private EventLogCreatedEventData data;
}
