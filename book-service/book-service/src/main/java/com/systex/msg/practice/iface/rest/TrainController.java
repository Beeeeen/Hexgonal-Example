package com.systex.msg.practice.iface.rest;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.sqs.model.SendMessageResult;
import com.systex.msg.base.iface.rest.dto.UUIDResource;
import com.systex.msg.practice.domain.train.aggregate.Train;
import com.systex.msg.practice.domain.train.command.CreateTrainCommand;
import com.systex.msg.practice.domain.train.command.QueryStopsCommand;
import com.systex.msg.practice.domain.train.command.QueryTrainCommand;
import com.systex.msg.practice.iface.rest.dto.CreateTrainResource;
import com.systex.msg.practice.iface.rest.dto.TrainResource;
import com.systex.msg.practice.iface.rest.dto.TrainStopsResource;
import com.systex.msg.practice.service.TrainCommandService;
import com.systex.msg.practice.service.TrainQueryService;
import com.systex.msg.practice.service.outbound.AwsSqsClient;
import com.systex.msg.util.BaseDataTransformer;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RequestMapping("/train")
@RestController
@Validated
public class TrainController {
	@Autowired
	TrainQueryService queryService; // Application Service Dependency
	@Autowired
	TrainCommandService commadService; // Application Service Dependency
	
	
	static {
		BaseDataTransformer.addConverter(TrainStopsResource.getConverter(), Train.class, TrainStopsResource.class);
	}
	static {
		BaseDataTransformer.addConverter(TrainResource.getConverter(), Train.class, TrainResource.class);
	}
	@GetMapping(path="/{trainNo}/stops") 
	public TrainStopsResource getStopsByTrainNo(
			@Valid @NotNull @Min(value = 1,message="車次必須為正整數") 
			@PathVariable("trainNo") Integer trainNo) {
		
		// DTO 防腐處理 (DTO > Command)
		QueryStopsCommand command = new QueryStopsCommand(trainNo);
		
		// 呼叫 Application Service
		Train train = queryService.query(command);
		
		// DTO 防腐處理 (Domain > DTO)，並回傳
		return BaseDataTransformer.transformAggregate(train, TrainStopsResource.class);
	}
	@GetMapping() 
	public List<TrainResource> getTrainByVia(
			@RequestParam("via") String via) {
		
		// DTO 防腐處理 (DTO > Command)
		QueryTrainCommand command = new QueryTrainCommand(via);

		// 呼叫 Application Service
		List<Train> train = queryService.query(command);
		
		
		// DTO 防腐處理 (Domain > DTO)，並回傳
		return BaseDataTransformer.transformAggregate(train, TrainResource.class);
	}
	
	@PostMapping()
	public UUIDResource create(@Valid @RequestBody CreateTrainResource resource) throws Exception {
		
		// DTO 防腐處理 (DTO > Command)
		//CreateTrainCommand command = new CreateTrainCommand(resource);
		// DTO 防腐處理 (DTO > Command)
		CreateTrainCommand command = BaseDataTransformer.transformDTO(resource, CreateTrainCommand.class);
		
		// 呼叫 Application Service
		Train train = commadService.create(command);
		
		// DTO 防腐處理 (Domain > DTO)，並回傳
		return BaseDataTransformer.transformAggregate(train.getUuid(), UUIDResource.class);
	}
	//test
	@PostMapping("/SQS")
	public SendMessageResult awsSQSPublishTest() throws InterruptedException, ExecutionException {
	
		//Publish message
		AwsSqsClient client = new AwsSqsClient();
		Future<SendMessageResult> future= client.SendMessage(
				"https://sqs.us-west-1.amazonaws.com/430188351668/myFifoQueue","testMessageBody");
		log.info("Waiting for future");
		while(future.isDone() == false) {
			log.info("Waiting for SendMessage");
			try {
				Thread.sleep(1000);
			}catch(InterruptedException e){
				log.error("InterruptedException");
				System.exit(-1);
			}
		}
		return future.get();
	}
	/*@GetMapping("/SQS")
	public List<Message> awsSQSConsumeTest(){
		AwsSqsClient client = new AwsSqsClient();

		return client.ReceiveMessage();
	}*/
	
}
