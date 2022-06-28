package com.systex.msg.practice.domain.ticket.command;


import java.math.BigDecimal;

import com.systex.msg.base.domain.share.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketCommand {

	private Integer train_no;
	private String from_stop;
	private String to_stop;
	private String take_date;
	
	private UUID trainUUID;
	private BigDecimal price;
}
