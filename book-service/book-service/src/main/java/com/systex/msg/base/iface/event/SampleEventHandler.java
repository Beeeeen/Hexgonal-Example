package com.systex.msg.base.iface.event;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.systex.msg.base.domain.command.FooSampleCommand;
import com.systex.msg.base.domain.outbound.SampleEvent;
import com.systex.msg.base.domain.outbound.SampleEventData;
import com.systex.msg.base.infra.broker.SampleEventSource;
import com.systex.msg.base.service.SampleCommandService;
import com.systex.msg.util.BaseDataTransformer;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@EnableBinding(SampleEventSource.class)
@AllArgsConstructor
public class SampleEventHandler extends BaseEventHandler {

	SampleCommandService service;

	@StreamListener(target = SampleEventSource.Topic.SAMPLE_SUB)
	public void receiveEvent(SampleEvent event) {
		SampleEventData eventData = event.getData();
		log.info("[EVENT] receive event, data: {}", eventData);

		// 防腐處理
		FooSampleCommand command = BaseDataTransformer.transformDTO(eventData, FooSampleCommand.class);

		// 冪等機制，防止重覆消費所帶來的副作用
		this.setEventIdempotent(command, event);
		
		// 呼叫 Application Service
		service.foo(command);
	}

}
