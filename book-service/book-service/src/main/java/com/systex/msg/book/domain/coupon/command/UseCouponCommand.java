package com.systex.msg.book.domain.coupon.command;

import com.systex.msg.base.domain.command.EventIdempotent;
import com.systex.msg.base.domain.share.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UseCouponCommand extends EventIdempotent {

	private UUID couponNo;
	private UUID usedTo;

}
