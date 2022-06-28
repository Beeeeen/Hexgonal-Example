package com.systex.msg.base.domain.outbound;

import com.systex.msg.base.domain.command.FooSampleCommand;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SampleEventData {

	private String foo;
	
	public SampleEventData(FooSampleCommand command) {
		this.foo = command.getFoo();
	}
	
}
