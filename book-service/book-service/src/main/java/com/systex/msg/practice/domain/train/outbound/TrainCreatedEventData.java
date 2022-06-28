package com.systex.msg.practice.domain.train.outbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainCreatedEventData {

	
	private Integer trainNo;
	private String trainKind;
}
