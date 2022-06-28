package com.systex.msg.practice.iface.rest.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.AbstractConverter;

import com.systex.msg.practice.domain.train.aggregate.Train;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Resource class for the Command API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainStopsResource {

	private Integer train_no;
	private String train_kind;
	//private List<TrainStop> Stops;
	private List<Map<String, String>> stops;
	
	public static AbstractConverter<Train, TrainStopsResource> getConverter() {
		return new AbstractConverter<Train, TrainStopsResource>() {
			protected TrainStopsResource convert(Train source) {
				if (source == null) {
					return null;
				}

				TrainStopsResource target = new TrainStopsResource();
				
				target.setTrain_no(source.getNumber());
				target.setTrain_kind(source.getKind().getLabel());
				//target.setStops(source.getStops());
				
				target.stops = new ArrayList<Map<String,String>>();//new
				
				//format time
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
				
				//format TrainStop
				for (int i =0;i<source.getStops().size();i++) {
					Map<String, String> TrainStopMap = new HashMap<>();
			        TrainStopMap.put("stop_name", source.getStops().get(i).getName());
			        TrainStopMap.put("stop_time", formatter.format(source.getStops().get(i).getTime()));
			        target.stops.add(TrainStopMap);
				}
		        
					
				//target.setStops(source.getStops());
				
				return target;
			}
		};
	}
	
}
