package com.systex.msg.base.domain.idempotentlog.aggregate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.springframework.data.domain.AbstractAggregateRoot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(EventIdempotentLogId.class)
@Table(name = "event_idempotent_log")
public class EventIdempotentLog extends AbstractAggregateRoot<EventIdempotentLog> {

	@Getter
	@Id
	@Column(name = "event_type")
	private String eventType;

	@Getter
	@Id
	@Column(name = "unique_key")
	private String uniqueKey;

	@Getter
	@Column(name = "target_id")
	private String targetId;

	@Getter
	@Column(name = "created_date")
	private Date createdDate;

	public EventIdempotentLog (String eventType, String uniqueKey) {
		this.eventType = eventType;
		this.uniqueKey = uniqueKey;
	}

}
