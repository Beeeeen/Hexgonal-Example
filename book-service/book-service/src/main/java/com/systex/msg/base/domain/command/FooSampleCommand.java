package com.systex.msg.base.domain.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FooSampleCommand extends EventIdempotent {

	private String foo;
	
}
