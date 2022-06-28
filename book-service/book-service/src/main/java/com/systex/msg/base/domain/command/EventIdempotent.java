package com.systex.msg.base.domain.command;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class EventIdempotent {

	String eventIdempotentType = null;
	String eventIdempotentKey = null;

}
