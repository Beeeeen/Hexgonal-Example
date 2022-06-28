package com.systex.msg.base.domain.eventlog.aggregate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.systex.msg.base.domain.BaseAggregate;
import com.systex.msg.base.domain.command.CreateEventLogCommand;
import com.systex.msg.base.domain.eventlog.aggregate.vo.EventLogSendQueueStatus;
import com.systex.msg.base.domain.share.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_log")
public class EventLog extends BaseAggregate<EventLog> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Embedded
	@Getter
	private UUID uuid; // Aggregate Identifier
	
	@Getter
	@Column(name = "user_id")
	private String userId;
	
	@Getter
	@Column(name = "event_class_name")
	private String eventClassName;
	
	@Getter
	@Column(name = "occured_at")
	private Date occuredAt;
	
	@Getter
	@Column(name = "target_id")
	private String targetId;
	
	@Getter
	private String body;
	
	@Getter
	@Column(name = "send_queue_status")
	private EventLogSendQueueStatus sendQueueStatus;
	
	/**
	 * Constructor Command Handler. registers Event
	 */
	public EventLog(UUID eventLogId, CreateEventLogCommand command) {
		this(eventLogId, command, EventLogSendQueueStatus.INITIAL);
	}

	/**
	 * Constructor Command Handler. registers Event
	 */
	public EventLog(UUID eventLogId, CreateEventLogCommand command, EventLogSendQueueStatus status) {
		this.uuid = eventLogId;
		this.userId = command.getUserId();
		this.eventClassName = command.getEventClassName();
		this.occuredAt = command.getOccuredAt();
		this.targetId = command.getTargetId();
		this.body = command.getBody();
		this.sendQueueStatus = status;
	}
	
	public void queueSended() {
		this.sendQueueStatus = EventLogSendQueueStatus.SENDED;
	}

}
