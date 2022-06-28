package com.systex.msg.base.domain.service;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleServiceTest {

	@Resource
	private SampleService service;

	@Test
	public void checkSomething() {
		boolean result1 = service.checkSomething("A", "A");
		boolean result2 = service.checkSomething("A", "B");

		Assertions.assertEquals(true, result1);
		Assertions.assertEquals(false, result2);
	}

}
