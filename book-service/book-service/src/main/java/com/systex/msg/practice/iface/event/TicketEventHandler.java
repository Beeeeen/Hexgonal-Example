package com.systex.msg.practice.iface.event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.base.iface.event.BaseEventHandler;
import com.systex.msg.practice.domain.ticket.command.ReleaseTicketCommand;
import com.systex.msg.practice.domain.train.outbound.TicketCreatedEvent;
import com.systex.msg.practice.domain.train.outbound.TicketCreatedEventData;
import com.systex.msg.practice.service.TicketCommandService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@AllArgsConstructor
@Service
public class TicketEventHandler extends BaseEventHandler{

	TicketCommandService service;
	@SqsListener(value ="https://sqs.us-west-1.amazonaws.com/430188351668/TicketQueue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void receiveEvent(String eventData) throws JsonMappingException, JsonProcessingException{
		log.info("[EVENT] receive event, data: {}",eventData);
		
		//parse json
		ObjectMapper mapper = new ObjectMapper();
		JsonNode EventLogData = mapper.readTree(eventData);
		JsonNode Data = EventLogData.get("data");
		
		
		String month = Data.get("takeDate").get("monthValue").asText();
		if(month.length()<=1){
			month="0"+month;
		}
		String day = Data.get("takeDate").get("dayOfMonth").asText();
		if(day.length()<=1){
			day="0"+day;
		}
		
		log.info("[EVENT] receive event, data: {}","\n恭禧您訂票成功，以下為車票資訊\n"
				+"車次:"+Data.get("trainNo")+" "+Data.get("from")+" 到 "+Data.get("to")+"\n"
				+"乘車日期:"+Data.get("takeDate").get("year")+"-"+month+"-"+day+"\n"
				+"票價:"+Data.get("price")
				);
		
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date = Data.get("takeDate").get("year")+"-"+month+"-"+day;
		LocalDate takeDate = LocalDate.parse(date, formatter);
		
		TicketCreatedEvent event = TicketCreatedEvent.builder()
				.eventLogUuid(new UUID(EventLogData.get("eventLogUuid").get("value").asText()))
				.targetId(EventLogData.get("targetId").asText())
				.data(new TicketCreatedEventData(Data.get("trainNo").asInt(),Data.get("from").asText()
						,Data.get("to").asText(),takeDate
						,Data.get("price").asInt()
						))
				.build();
		
		
		
		// 防腐處理
		ReleaseTicketCommand command = new ReleaseTicketCommand(EventLogData.get("targetId").asText());

		// 冪等機制，防止重覆消費所帶來的副作用
		this.setEventIdempotent(command,event.getClass().getName(),event.getTargetId());
		
		// 呼叫 Application Service
		service.release(command);
		
	}
}
