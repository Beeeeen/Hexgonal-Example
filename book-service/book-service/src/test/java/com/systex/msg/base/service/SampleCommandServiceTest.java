package com.systex.msg.base.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.systex.msg.base.domain.command.FooSampleCommand;
import com.systex.msg.base.domain.sample.aggregate.Sample;
import com.systex.msg.base.infra.repository.EventIdempotentLogRepository;
import com.systex.msg.base.infra.repository.EventLogRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleCommandServiceTest {

	@Autowired
	private SampleCommandService service;

	@MockBean
	private EventLogRepository eventLogRepository;
	
	@MockBean
	private EventIdempotentLogRepository eventIdempotentLogRepository;

	@MockBean
	private RocketMQTemplate temp;
	
//	@MockBean
//	private SampleOutboundService outboundService;
	
//	@MockBean
//	private SamplePublisherService publisherService;

//	@Autowired
//	private SampleService domainService;
	
	@Before
	public void setup() {
		when(eventIdempotentLogRepository.findOneByEventTypeAndUniqueKey(any(), any())).thenReturn(null);
	}

	@Test
	@Transactional
	public void foo() {
		final String foo = "TEST";
		
		FooSampleCommand command = new FooSampleCommand();
		command.setFoo(foo);
		Sample sample = service.foo(command);
		
		Assert.assertEquals(foo, sample.getFoo());
	}

	@Test
	@Transactional
	public void run() {
		boolean result = service.run();
		Assert.assertEquals(true, result);
	}
}