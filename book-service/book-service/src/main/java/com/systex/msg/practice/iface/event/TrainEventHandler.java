package com.systex.msg.practice.iface.event;

import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.msg.exception.ServiceRuntimeException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component

public class TrainEventHandler{

	@SqsListener(value ="https://sqs.us-west-1.amazonaws.com/430188351668/myFifoQueue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void receiveEvent(String eventData) throws JsonMappingException, JsonProcessingException ,ServiceRuntimeException{
		//log.info("[EVENT] receive event, data: {}",eventData);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode EventLogData = mapper.readTree(eventData);
		JsonNode Data = EventLogData.get("data");
		
		Integer trainNo = EventLogData.get("data").get("trainNo").asInt();
		if(trainNo%2 ==0) {
			throw new ServiceRuntimeException("車次偶數模擬發生系統性錯誤");
		}
		log.info("[EVENT] receive event, data: {}", Data.get("trainKind").asText()
				+Data.get("trainNo").toString()+"車次增加成功");
	
		
	}
}
