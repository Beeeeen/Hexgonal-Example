package com.systex.msg.practice.iface.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.systex.msg.base.iface.rest.dto.UUIDResource;
import com.systex.msg.practice.domain.ticket.aggregate.Ticket;
import com.systex.msg.practice.domain.ticket.command.CreateTicketCommand;
import com.systex.msg.practice.iface.rest.dto.CreateTicketResource;
import com.systex.msg.practice.service.TicketCommandService;
import com.systex.msg.util.BaseDataTransformer;

@RequestMapping("/ticket")
@RestController
@Validated
public class TicketController {

	@Autowired
	TicketCommandService commadService; // Application Service Dependency
	@PostMapping()
	public UUIDResource create(@Valid @RequestBody CreateTicketResource resource) throws Exception {
		// DTO 防腐處理 (DTO > Command)
		CreateTicketCommand command = BaseDataTransformer.transformDTO(resource, CreateTicketCommand.class);
		
		// 呼叫 Application Service
		Ticket ticket = commadService.create(command);
		
		// DTO 防腐處理 (Domain > DTO)，並回傳
		return BaseDataTransformer.transformAggregate(ticket.getTicketNo(), UUIDResource.class);
		
	}
}
