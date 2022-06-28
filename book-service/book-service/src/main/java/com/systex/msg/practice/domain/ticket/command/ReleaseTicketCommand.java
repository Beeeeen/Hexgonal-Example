package com.systex.msg.practice.domain.ticket.command;

import com.systex.msg.base.domain.command.EventIdempotent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseTicketCommand extends EventIdempotent {

	private String ticket_no;

}
