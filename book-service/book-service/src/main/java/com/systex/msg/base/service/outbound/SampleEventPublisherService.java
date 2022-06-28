package com.systex.msg.base.service.outbound;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.systex.msg.base.domain.eventlog.aggregate.EventLog;
import com.systex.msg.base.domain.outbound.SampleEvent;
import com.systex.msg.base.infra.broker.SampleEventSource;
import com.systex.msg.base.infra.repository.EventLogRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
@AllArgsConstructor
public class SampleEventPublisherService extends BasePublisherService {

	SampleEventSource eventSource;

	EventLogRepository eventLogRepository;

	@TransactionalEventListener
	public void handleSampleEvent(SampleEvent event) {
		try {
			log.info("[EVENT] send event, data: {}", event.getData());
			boolean sent = eventSource.samplePub().send(MessageBuilder.withPayload(event).build());
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
