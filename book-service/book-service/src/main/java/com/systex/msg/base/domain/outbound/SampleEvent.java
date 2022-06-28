package com.systex.msg.base.domain.outbound;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class SampleEvent extends BaseEvent {

	@Getter
	private SampleEventData data;

}
