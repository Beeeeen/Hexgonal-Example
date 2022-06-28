package com.systex.msg.base.infra.repository;

import java.util.Optional;

import javax.annotation.Resource;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.systex.msg.base.domain.sample.aggregate.Sample;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleRepositoryTest {

    @Resource
	private SampleRepository repository;
	
	@Test
	@Transactional
	public void findById() {
		
		Optional<Sample> sample = repository.findById(1l);
		Assertions.assertThat(sample).isNotEmpty();
		
	}
	
}