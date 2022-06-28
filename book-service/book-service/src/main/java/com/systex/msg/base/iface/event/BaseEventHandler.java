package com.systex.msg.base.iface.event;

import com.systex.msg.base.domain.command.EventIdempotent;
import com.systex.msg.base.domain.outbound.BaseEvent;

public class BaseEventHandler {

	protected void setEventIdempotent(EventIdempotent command, BaseEvent event) {
		this.setEventIdempotent(command, event.getClass().getName(), event.getTargetId());
	}

	protected void setEventIdempotent(EventIdempotent command, BaseEvent event, String eventIdempotentKey) {
		this.setEventIdempotent(command, event.getClass().getName(), eventIdempotentKey);
	}

	protected void setEventIdempotent(EventIdempotent command, String eventIdempotentType, String eventIdempotentKey) {
		command.setEventIdempotentType(eventIdempotentType);
		command.setEventIdempotentKey(eventIdempotentKey);
	}

}
