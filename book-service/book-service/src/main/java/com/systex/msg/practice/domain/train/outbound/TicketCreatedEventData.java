package com.systex.msg.practice.domain.train.outbound;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketCreatedEventData {

	private Integer trainNo;
	private String from;
	private String to;
	@Getter
    private TakeDate takeDate;
    private Integer price;
    public TicketCreatedEventData(Integer trainNo,String from,String to,LocalDate takeDate,Integer price) {
    	
    	this.trainNo = trainNo;
    	this.from = from;
    	this.to = to;
    	this.takeDate = new TakeDate(takeDate);
    	this.price = price;
    	
    }
}
