package com.systex.msg.practice.service;

import java.io.IOException;
import java.util.Optional;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.systex.msg.base.domain.AggregateProxyFactory;
import com.systex.msg.base.domain.eventlog.aggregate.EventLog;
import com.systex.msg.base.domain.outbound.BaseEvent;
import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.base.infra.repository.EventLogRepository;
import com.systex.msg.base.service.BaseApplicationService;
import com.systex.msg.config.ContextHolder;
import com.systex.msg.exception.NotFoundException;
import com.systex.msg.exception.ValidateFailedException;
import com.systex.msg.practice.domain.service.TrainService;
import com.systex.msg.practice.domain.ticket.aggregate.Ticket;
import com.systex.msg.practice.domain.ticket.command.CreateTicketCommand;
import com.systex.msg.practice.domain.ticket.command.ReleaseTicketCommand;
import com.systex.msg.practice.domain.train.aggregate.Train;
import com.systex.msg.practice.iface.event.TrainEventHandler;
import com.systex.msg.practice.infra.repository.TicketRepository;
import com.systex.msg.practice.service.outbound.ExternalService;
import com.systex.msg.util.BaseDataTransformer;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
@AllArgsConstructor
@Log4j2
public class TicketCommandService extends BaseApplicationService{
	private TicketRepository ticketRepository;
	private TrainService domainService;
	private EventLogRepository eventLogRepository;
	private ExternalService externalService;
	/**
	 * Service Command method to create
	 * 
	 * @param command
	 * @return
	 * @throws ValidateFailedException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public Ticket create(CreateTicketCommand command) throws ValidateFailedException, IOException {
		
		
		
		//實做領域檢核(跨aggregate 的檢核)
		Train train = domainService.checkTrainNoExist(command);//check is train_no exist 
		domainService.checkStopValid(command, train);//check order of trainStop 
		command.setTrainUUID(train.getUuid());//組裝資訊
		
		//call OutBound service
		command.setPrice(externalService.callPriceServiceAPI(command));
		

		// 叫用 Command Handler
		Ticket ticket = new Ticket();
		Ticket proxy = AggregateProxyFactory.getProxyInstance(Ticket.class, ticket);
		proxy.create(command);
		
		// 儲存 Aggregate
		ticketRepository.save(ticket);
		
		// 寫入 EventLog（當有 Next Event 需要發佈時）
		BaseEvent event = ContextHolder.getEvent();
		EventLog eventLog = new EventLog(event.getEventLogUuid(), BaseDataTransformer.transformEvent(event));
		eventLogRepository.save(eventLog);
		
		
		return ticket;
	
	}
	/**
	 * Service Command method to patch
	 * 
	 * @return Ticket
	 */
	public Ticket release(ReleaseTicketCommand command) {
		
		// 冪等機制，防止重覆消費所帶來的副作用
		if (!this.checkEventIdempotent(command))
			return null; 
		
		// 取得本次交易 Aggregate
		Optional<Ticket> opt = Optional.ofNullable(ticketRepository.findByTicketNo(new UUID(command.getTicket_no())));
		if (!opt.isPresent())
			throw new NotFoundException(String.format("ticket not found (%s)", command.getTicket_no()));

		// 叫用 Command Handler
		Ticket ticket = opt.get();
		Ticket proxy = AggregateProxyFactory.getProxyInstance(Ticket.class, ticket);
		proxy.release(command);
		
		// 儲存 Aggregate
		ticketRepository.save(ticket);

		// 冪等機制，防止重覆消費所帶來的副作用
		this.insertEventIdempotent(command);
		
		return ticket;
	}
	
	
}
