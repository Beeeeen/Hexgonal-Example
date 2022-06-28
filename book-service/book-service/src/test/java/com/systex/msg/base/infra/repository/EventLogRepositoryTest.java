package com.systex.msg.base.infra.repository;

import java.util.Date;

import javax.annotation.Resource;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.systex.msg.base.domain.command.CreateEventLogCommand;
import com.systex.msg.base.domain.eventlog.aggregate.EventLog;
import com.systex.msg.base.domain.eventlog.aggregate.vo.EventLogSendQueueStatus;
import com.systex.msg.base.domain.share.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventLogRepositoryTest {

	@Resource
	private EventLogRepository repository;
	
	
	@Test
	public void testFindByUuid() {
		System.out.println("first");
		
		CreateEventLogCommand command = new CreateEventLogCommand("1","UserCreatedEvent",new Date(),"1","{id=1,username=\"Till\",firstname=\"Till\",lastname=\"Tsai\"}");
		EventLog eventLog = new EventLog(new UUID(), command);
		repository.save(eventLog);
		Assertions.assertThat(eventLog.getUuid()).isNotNull();
		
		System.out.println("second");
		
		UUID uuid = eventLog.getUuid();

		EventLog evenctLog = repository.findByUuid(uuid);
		Assertions.assertThat(evenctLog).isNotNull();
		Assertions.assertThat(uuid.getValue()).isEqualTo(evenctLog.getUuid().getValue());
	}
	
	@Test
	public void testQueueSended() {
		System.out.println("third");
		
		CreateEventLogCommand command = new CreateEventLogCommand("1","UserCreatedEvent",new Date(),"1","{id=1,username=\"Till\",firstname=\"Till\",lastname=\"Tsai\"}");
		EventLog eventLog = new EventLog(new UUID(), command);
		repository.save(eventLog);
	

		eventLog.queueSended();
		repository.save(eventLog);
		Assertions.assertThat(EventLogSendQueueStatus.SENDED).isEqualTo(eventLog.getSendQueueStatus());
	}

}