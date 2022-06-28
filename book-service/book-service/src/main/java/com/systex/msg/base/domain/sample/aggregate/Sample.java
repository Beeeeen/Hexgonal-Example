package com.systex.msg.base.domain.sample.aggregate;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.systex.msg.base.domain.BaseAggregate;
import com.systex.msg.base.domain.command.FooSampleCommand;
import com.systex.msg.base.domain.outbound.BaseEvent;
import com.systex.msg.base.domain.outbound.SampleEvent;
import com.systex.msg.base.domain.outbound.SampleEventData;
import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.config.ContextHolder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sample")
public class Sample extends BaseAggregate<Sample> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Embedded
	@Getter
	@AttributeOverride(name = "value", column = @Column(name = "uuid"))
	private UUID uuid; // Aggregate Identifier
	
	@Getter
	private String foo;
	
	public void foo(FooSampleCommand command) {
		this.uuid = new UUID();
		this.foo = command.getFoo();
		
		// 註冊 Domain Event（當有 Next Event 需要發佈時）
		BaseEvent event = SampleEvent.builder().eventLogUuid(new UUID()).targetId(this.uuid.getValue())
				.data(new SampleEventData(command)).build();
		ContextHolder.setEvent(event);
		addDomainEvent(event);
	}

}
