package com.systex.msg.base.iface.event;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Service;

import com.systex.msg.base.infra.broker.SampleEventSource;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@EnableBinding(SampleEventSource.class)
@AllArgsConstructor
public class ErrorEventHandler {

	/**
	 * A Method of Handling Global Exceptions
	 *
	 * @param errorMessage Exception message object
	 */
	@StreamListener("errorChannel")
	public void handleError(ErrorMessage errorMessage) {
	    log.error("exception occurred. errorMessage = {}", errorMessage);
	}
	

}
