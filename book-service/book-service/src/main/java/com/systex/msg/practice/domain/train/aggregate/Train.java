package com.systex.msg.practice.domain.train.aggregate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.systex.msg.base.domain.BaseAggregate;
import com.systex.msg.base.domain.outbound.BaseEvent;
import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.config.ContextHolder;
import com.systex.msg.exception.ValidateFailedException;
import com.systex.msg.exception.ValidateFailedException.DomainErrorStatus;
import com.systex.msg.practice.domain.train.aggregate.entity.TrainStop;
import com.systex.msg.practice.domain.train.aggregate.vo.TrainKind;
import com.systex.msg.practice.domain.train.command.CreateTrainCommand;
import com.systex.msg.practice.domain.train.outbound.TrainCreatedEvent;
import com.systex.msg.practice.domain.train.outbound.TrainCreatedEventData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "train")
public class Train extends BaseAggregate<Train> {

	@Id
	@GeneratedValue(generator = "uuid-generator")
	@GenericGenerator(name = "uuid-generator", parameters = @Parameter(name = "column", value = "u"), strategy = "com.systex.msg.base.domain.share.UUIDGenerator")
	private UUID uuid;

	@Transient
	private UUID u;

	@Column(name = "train_no")
	private Integer number;

	@Column(name = "train_kind")
	private TrainKind kind;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinColumn(name = "train_uuid", updatable = false)
	private List<TrainStop> stops;

	/**
	 * Constructor Command Handler. registers Event
	 */
	public void create(CreateTrainCommand command) {
		
		this.u = new UUID();
		this.number=command.getTrain_no();
		this.kind=TrainKind.fromLabel(command.getTrain_kind());
		
		stops = new ArrayList<TrainStop>();
		for(int i =0 ;i<command.getStops().size();i++)
		{
			TrainStop stop = new TrainStop();
			stop.create(this.u,i+1, command.getStops().get(i).get("stop_name"), command.getStops().get(i).get("stop_time"));
			stops.add(stop);
		}
		
		
		// 註冊 Domain Event（當有 Next Event 需要發佈時）
		BaseEvent event = TrainCreatedEvent.builder().eventLogUuid(new UUID()).targetId(this.u.getValue())
				.data(new TrainCreatedEventData(this.number,this.kind.getLabel())).build();
		ContextHolder.cleanupEventLogHolder();
		ContextHolder.setEvent(event);
		addDomainEvent(event);
		
	}
	
	// 透過 Aggregate check method 進行領域檢核
	public void checkCreate(List<Object> arguments) throws ValidateFailedException {
		CreateTrainCommand command = (CreateTrainCommand) arguments.get(0);
		//check stops is sorted by time
		for(int i =0 ;i<command.getStops().size()-1;i++) {
			//get this stop_time and next stop_time
			String stopTime = command.getStops().get(i).get("stop_time").toString();
			String stopTimeNext = command.getStops().get(i+1).get("stop_time").toString();
			
			//remove ':'
			stopTime = stopTime.replaceAll(":", "");
			stopTimeNext =stopTimeNext.replaceAll(":", "");
			if(Integer.parseInt(stopTime)>Integer.parseInt(stopTimeNext)) {
				throw new ValidateFailedException(DomainErrorStatus.TRAIN_STOP_SORTED);
			}
		}
	}
	
	
}
