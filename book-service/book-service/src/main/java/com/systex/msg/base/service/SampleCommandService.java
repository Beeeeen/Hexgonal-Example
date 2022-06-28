package com.systex.msg.base.service;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.systex.msg.base.domain.AggregateProxyFactory;
import com.systex.msg.base.domain.command.FooSampleCommand;
import com.systex.msg.base.domain.eventlog.aggregate.EventLog;
import com.systex.msg.base.domain.outbound.BaseEvent;
import com.systex.msg.base.domain.sample.aggregate.Sample;
import com.systex.msg.base.domain.service.SampleService;
import com.systex.msg.base.infra.repository.EventLogRepository;
import com.systex.msg.base.infra.repository.SampleRepository;
import com.systex.msg.config.ContextHolder;
import com.systex.msg.exception.ServiceRuntimeException;
import com.systex.msg.util.BaseDataTransformer;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Application Service class for the Commands
 */
@Log4j2
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
@AllArgsConstructor
public class SampleCommandService extends BaseApplicationService {

	private SampleService service;
	private SampleRepository repository;
	private EventLogRepository eventLogRepository;

	public Sample foo(FooSampleCommand command) {
		
		// 冪等機制，防止重覆消費所帶來的副作用
		if (!this.checkEventIdempotent(command))
			return null;
		
		log.debug("foo start");

		// 叫用領域檢核，當檢核失敗時，進行失敗的處理，不丟出 Exception
		if (service.checkSomething("expect", command.getFoo())) {
			log.error("check failed. foo is {}", command.getFoo());
			return null;
		}

		// 若出現系統錯誤，則丟出 RuntimeException，進行 re-consume
		if (command.getFoo() == null)
			throw new ServiceRuntimeException("command is null");
		
		// 叫用 Command Handler
		Sample sample = new Sample();
		Sample proxy = AggregateProxyFactory.getProxyInstance(Sample.class, sample);
		proxy.foo(command);
		
		// 儲存 Aggregate
		repository.save(sample);
		
		// 寫入 EventLog（當有 Next Event 需要發佈時）
		BaseEvent event = ContextHolder.getEvent();
		EventLog eventLog = new EventLog(event.getEventLogUuid(), BaseDataTransformer.transformEvent(event));
		eventLogRepository.save(eventLog);
		
		// 冪等機制，防止重覆消費所帶來的副作用
		this.insertEventIdempotent(command);

		return sample;
	}

	public boolean run() {

		log.debug("run start");

		FooSampleCommand command = new FooSampleCommand();
		command.setFoo("run " + RandomUtils.nextInt(1, 65536));
		
		Sample sample = new Sample();
		Sample proxy = AggregateProxyFactory.getProxyInstance(Sample.class, sample);
		proxy.foo(command);
		
		repository.save(sample);
		
		BaseEvent event = ContextHolder.getEvent();
		EventLog eventLog = new EventLog(event.getEventLogUuid(), BaseDataTransformer.transformEvent(event));
		eventLogRepository.save(eventLog);
		
		return true;
	}

}
