package com.systex.msg.base.infra.broker;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Interface depicting all output channels
 */
public interface SampleEventSource {

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	final class Topic {
		public static final String SAMPLE_PUB = "sample-pub";
		public static final String SAMPLE_SUB = "sample-sub";
	}

	@Output(Topic.SAMPLE_PUB)
	MessageChannel samplePub();

	@Input(Topic.SAMPLE_SUB)
	SubscribableChannel sampleSub();


}
