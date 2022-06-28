package com.systex.msg.practice.service.outbound;

import java.util.concurrent.Future;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.amazonaws.services.sqs.model.SendMessageResult;
import com.systex.msg.base.domain.eventlog.aggregate.EventLog;
import com.systex.msg.base.infra.repository.EventLogRepository;
import com.systex.msg.base.service.outbound.BasePublisherService;
import com.systex.msg.practice.domain.train.outbound.TrainCreatedEvent;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
@AllArgsConstructor
public class TrainPublisherService extends BasePublisherService{

	EventLogRepository eventLogRepository;
	
	
	@TransactionalEventListener//called When transact successfully
	public void handleTrainCreatedEvent(TrainCreatedEvent event) {
		try {
		
			EventLog eventLog = eventLogRepository.findByUuid(event.getEventLogUuid());
			
			log.info("[EVENT] send event, data: {}", eventLog.getBody());
			
			
			//Publish message
			AwsSqsClient client = new AwsSqsClient();
			Future<SendMessageResult> future= client.SendMessage(
					"https://sqs.us-west-1.amazonaws.com/430188351668/myFifoQueue",eventLog.getBody());
			log.info("Waiting for SendMessage");
			while(future.isDone() == false) {
				log.info("Waiting for SendMessage");
				try {
					Thread.sleep(1000);
				}catch(InterruptedException e){
					log.error("InterruptedException");
					System.exit(-1);
				}
			}
			if(future.isDone() == true) {
				log.info("[EVENT] send event successfully, data: {}", eventLog.getBody());
			}
			
			
			/*boolean sent = eventSource.bookCreatingPub().send(MessageBuilder.withPayload(event).build());
			if (sent) {
				EventLog eventLog = eventLogRepository.findByUuid(event.getEventLogUuid());
				eventLog.queueSended();
				eventLogRepository.save(eventLog);
			}*/
		} catch (Exception ex) {
			log.error("error", ex);
		}
	}
	
}
