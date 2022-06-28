package com.systex.msg.practice.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.systex.msg.base.domain.AggregateProxyFactory;
import com.systex.msg.base.domain.eventlog.aggregate.EventLog;
import com.systex.msg.base.domain.outbound.BaseEvent;
import com.systex.msg.base.infra.repository.EventLogRepository;
import com.systex.msg.base.service.BaseApplicationService;
import com.systex.msg.config.ContextHolder;
import com.systex.msg.exception.ValidateFailedException;
import com.systex.msg.practice.domain.service.TrainService;
import com.systex.msg.practice.domain.train.aggregate.Train;
import com.systex.msg.practice.domain.train.command.CreateTrainCommand;
import com.systex.msg.practice.infra.repository.TrainRepository;
import com.systex.msg.practice.service.outbound.ExternalService;
import com.systex.msg.util.BaseDataTransformer;

import lombok.AllArgsConstructor;


/**
 * Application Service class for the Commands
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
@AllArgsConstructor
public class TrainCommandService extends BaseApplicationService{
	private TrainRepository trainRepository;
	private EventLogRepository eventLogRepository;
	private TrainService domainService;
	private ExternalService externalService;
	/**
	 * Service Command method to create
	 * 
	 * @param command
	 * @return
	 * @throws ValidateFailedException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public Train create(CreateTrainCommand command) throws ValidateFailedException, ClientProtocolException, IOException {
		
		externalService.callStatusServiceAPI(command);//Call status-service API to validate train_no
		
		//實做領域叫用 Domain Service 進行領域檢核(本身aggregate 可進行的檢核)
		domainService.checkCreateTrainCommand(command);//組合式檢核實作在 Domain Service

		
		// 叫用 Command Handler
		Train train = new Train();
		Train proxy = AggregateProxyFactory.getProxyInstance(Train.class, train);
		proxy.create(command);
		
		// 儲存 Aggregate
		trainRepository.save(train);
		
		// 寫入 EventLog（當有 Next Event 需要發佈時）
		BaseEvent event = ContextHolder.getEvent();
		EventLog eventLog = new EventLog(event.getEventLogUuid(), BaseDataTransformer.transformEvent(event));
		eventLogRepository.save(eventLog);
		
		
		return train;
		
	}
	
	
	
	
}
