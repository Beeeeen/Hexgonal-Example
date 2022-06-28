package com.systex.msg.base.domain.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class EventLogCreatedEventData {

	@Getter
	private String uuid;

}
