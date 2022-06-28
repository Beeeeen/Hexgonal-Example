package com.systex.msg.practice.domain.ticket.aggregate;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.http.client.ClientProtocolException;

import com.systex.msg.base.domain.BaseAggregate;
import com.systex.msg.base.domain.outbound.BaseEvent;
import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.config.ContextHolder;
import com.systex.msg.exception.ValidateFailedException;
import com.systex.msg.practice.domain.ticket.command.CreateTicketCommand;
import com.systex.msg.practice.domain.ticket.command.ReleaseTicketCommand;
import com.systex.msg.practice.domain.train.outbound.TicketCreatedEvent;
import com.systex.msg.practice.domain.train.outbound.TicketCreatedEventData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "train_ticket")
public class Ticket extends BaseAggregate<Ticket> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Embedded
	@Getter
	@AttributeOverride(name = "value", column = @Column(name = "ticket_no"))
	private UUID ticketNo; // Aggregate Identifier

	@Embedded
	@Getter
	@AttributeOverride(name = "value", column = @Column(name = "train_uuid"))
	private UUID trainUUID;

	@Getter
	@Column(name = "from_stop")
	private String fromStop;

	@Getter
	@Column(name = "to_stop")
	private String toStop;
	
	@Getter
	@Column(name = "take_date")
	private LocalDate takeDate;
	
	@Getter
	private BigDecimal price;
	
	
	//private TrainRepository trainRepository;
	/**
	 * Constructor Command Handler. registers Event
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws ValidateFailedException 
	 */
	public void create(CreateTicketCommand command) throws ClientProtocolException, IOException, ValidateFailedException {
		
		//this.trainUUID = new UUID(trainRepository.findUUIDBynumber(Integer.parseInt(command.getTrain_no()) ));
		
		this.trainUUID = command.getTrainUUID();
		
		this.ticketNo = new UUID();
		this.fromStop = command.getFrom_stop();
		this.toStop = command.getTo_stop();
		
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate takeDate = LocalDate.parse(command.getTake_date(),formatter);
		this.takeDate = takeDate;
		this.price = command.getPrice();
		
		
		// 註冊 Domain Event（當有 Next Event 需要發佈時）
		BaseEvent event = TicketCreatedEvent.builder().eventLogUuid(new UUID()).targetId(this.ticketNo.getValue())
				.data(new TicketCreatedEventData(command.getTrain_no(),this.fromStop
				,this.toStop,this.takeDate,this.price.intValue())).build();
		ContextHolder.cleanupEventLogHolder();
		ContextHolder.setEvent(event);
		addDomainEvent(event);
		
	}
	public void checkCreate(List<Object> arguments) throws ValidateFailedException  {
		CreateTicketCommand command = (CreateTicketCommand) arguments.get(0);
		
	}
	public void release(ReleaseTicketCommand command) {		
		//do noting
	}

}
