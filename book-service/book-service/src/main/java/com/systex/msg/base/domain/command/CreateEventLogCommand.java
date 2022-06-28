package com.systex.msg.base.domain.command;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventLogCommand {

	private String userId;
	private String eventClassName;
	private Date occuredAt;
	private String targetId;
	private String body;
	
}
