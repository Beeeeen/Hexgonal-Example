package com.systex.msg.practice.domain.train.command;

import java.util.List;
import java.util.Map;

import com.systex.msg.base.domain.command.EventIdempotent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTrainCommand extends EventIdempotent{


	private Integer train_no;
	private String train_kind;
	private List<Map<String, String>> stops;
	/*public Create(CreateTrainResource resource)
	{
		this.u=new UUID();
		this.number = resource.getTrain_no();
		this.kind = resource.getTrain_kind();
		this.stops = new ArrayList<TrainStop>();//new
		for(int i=0;i<resource.getStops().size();i++)
		{
			TrainStop trainStop = new TrainStop();
			trainStop.create(this.u,i+1,resource.getStops().get(i).get("stop_name")
					,resource.getStops().get(i).get("stop_time"));
			this.stops.add(trainStop);
		}
	}*/
	
}
