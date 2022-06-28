package com.systex.msg.practice.infra.broker;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Interface depicting all output channels
 */
public interface TrainEventSource {

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	final class Topic {
		public static final String TRAIN_CREATING_PUB = "trian-creating-pub";
		public static final String TRAIN_CREATING_SUB = "trian-creating-sub";
	}

	@Output(Topic.TRAIN_CREATING_PUB)
	MessageChannel trainCreatingPub();

	@Input(Topic.TRAIN_CREATING_SUB)
	SubscribableChannel trainCreatingSub();
}
