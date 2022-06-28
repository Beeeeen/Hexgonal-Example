package com.systex.msg.base.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.systex.msg.base.domain.command.EventIdempotent;
import com.systex.msg.base.domain.idempotentlog.aggregate.EventIdempotentLog;
import com.systex.msg.base.domain.outbound.BaseEvent;
import com.systex.msg.base.infra.repository.EventIdempotentLogRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class BaseApplicationService {

	protected static final int QUERY_PAGE_SIZE = 10;

	@Autowired
	private EventIdempotentLogRepository eventIdempotentLogRepository;

	protected void setEventIdempotent(EventIdempotent command, BaseEvent event, String eventIdempotentKey) {
		this.setEventIdempotent(command, event.getClass().getName(), eventIdempotentKey);
	}

	protected void setEventIdempotent(EventIdempotent command, String eventIdempotentType, String eventIdempotentKey) {
		command.setEventIdempotentType(eventIdempotentType);
		command.setEventIdempotentKey(eventIdempotentKey);
	}

	protected boolean checkEventIdempotent(EventIdempotent command) {
		final String eventIdempotentType = command.getEventIdempotentType();
		final String eventIdempotentKey = command.getEventIdempotentKey();
		EventIdempotentLog eiLog = eventIdempotentLogRepository.findOneByEventTypeAndUniqueKey(eventIdempotentType, eventIdempotentKey);
		if (eiLog != null)
			log.info("checkEventIdempotent: found {} {}", eiLog.getEventType(), eiLog.getUniqueKey());
		return eiLog == null;
	}

	protected void insertEventIdempotent(EventIdempotent command) {
		final String eventIdempotentType = command.getEventIdempotentType();
		final String eventIdempotentKey = command.getEventIdempotentKey();
		eventIdempotentLogRepository.insert(eventIdempotentType, eventIdempotentKey);
	}

}
