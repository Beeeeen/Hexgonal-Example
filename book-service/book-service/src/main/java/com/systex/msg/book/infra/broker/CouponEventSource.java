package com.systex.msg.book.infra.broker;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Interface depicting all output channels
 */
public interface CouponEventSource {

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	final class Topic {
		public static final String COUPON_USING_SUB = "coupon-using-sub";
	}

	@Input(Topic.COUPON_USING_SUB)
	SubscribableChannel couponUsingSub();

}
