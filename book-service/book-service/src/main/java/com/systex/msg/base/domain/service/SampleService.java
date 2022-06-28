package com.systex.msg.base.domain.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Domain Service
 */
@Log4j2
@Service
@AllArgsConstructor
public class SampleService {

	public boolean checkSomething(String s1, String s2) {
		log.debug("checkSomething s1:{} s2:{}", s1, s2);
		return StringUtils.equals(s1, s2);
	}

}
