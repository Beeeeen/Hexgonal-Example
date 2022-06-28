package com.systex.msg.practice.iface.rest.dto;

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
public class TrainResource {

	private Integer train_no;
	private String train_kind;
	
	public static AbstractConverter<Train, TrainResource> getConverter() {
		return new AbstractConverter<Train, TrainResource>() {
			protected TrainResource convert(Train source) {
				if (source == null) {
					return null;
				}

				TrainResource target = new TrainResource();
			
				target.setTrain_no(source.getNumber());
				target.setTrain_kind(source.getKind().getLabel());
				
				return target;
			}
		};
	}
}
