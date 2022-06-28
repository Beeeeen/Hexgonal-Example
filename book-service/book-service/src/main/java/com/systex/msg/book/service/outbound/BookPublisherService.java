package com.systex.msg.book.service.outbound;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.systex.msg.base.domain.eventlog.aggregate.EventLog;
import com.systex.msg.base.infra.repository.EventLogRepository;
import com.systex.msg.base.service.outbound.BasePublisherService;
import com.systex.msg.book.domain.book.outbound.BookCreatedEvent;
import com.systex.msg.book.domain.book.outbound.BookUpdatedEvent;
import com.systex.msg.book.infra.broker.BookEventSource;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
@AllArgsConstructor
public class BookPublisherService extends BasePublisherService {

	BookEventSource eventSource;

	EventLogRepository eventLogRepository;

	@TransactionalEventListener
	public void handleBookCreatedEvent(BookCreatedEvent event) {
		try {
			log.info("[EVENT] send event, data: {}", event.getData());
			boolean sent = eventSource.bookCreatingPub().send(MessageBuilder.withPayload(event).build());
			if (sent) {
				EventLog eventLog = eventLogRepository.findByUuid(event.getEventLogUuid());
				eventLog.queueSended();
				eventLogRepository.save(eventLog);
			}
		} catch (Exception ex) {
			log.error("error", ex);
		}
	}

	@TransactionalEventListener
	public void handleBookCreatedEvent(BookUpdatedEvent event) {
		try {
			log.info("[EVENT] send event, data: {}", event.getData());
			boolean sent = eventSource.bookUpdatingPub().send(MessageBuilder.withPayload(event).build());
			if (sent) {
				EventLog eventLog = eventLogRepository.findByUuid(event.getEventLogUuid());
				eventLog.queueSended();
				eventLogRepository.save(eventLog);
			}
		} catch (Exception ex) {
			log.error("error", ex);
		}
	}

}
