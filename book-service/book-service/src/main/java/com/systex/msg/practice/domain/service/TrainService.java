package com.systex.msg.practice.domain.service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.systex.msg.exception.ValidateFailedException;
import com.systex.msg.exception.ValidateFailedException.DomainErrorStatus;
import com.systex.msg.practice.domain.ticket.command.CreateTicketCommand;
import com.systex.msg.practice.domain.train.aggregate.Train;
import com.systex.msg.practice.domain.train.aggregate.entity.TrainStop;
import com.systex.msg.practice.domain.train.aggregate.vo.TrainKind;
import com.systex.msg.practice.domain.train.command.CreateTrainCommand;
import com.systex.msg.practice.infra.repository.TrainRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TrainService {

	private TrainRepository trainRepository;
	public Train findByNumber(Integer trainNo)
	{
		return trainRepository.findByNumber(trainNo);
	}
	public void checkCreateTrainCommand(CreateTrainCommand command) throws ValidateFailedException
	{
		ValidateFailedException e = new ValidateFailedException();
		
		DomainErrorStatus status = this.checkTrainNoExist(command);
		if(status!=null){
			e.add(status);
		}
		status = this.checkTrainKindValid(command);
		if(status!=null){
			e.add(status);
		}
		status = this.checkTrainStopDuplicate(command);
		if(status!=null){
			e.add(status);
		}
		if(e.hasErrors()) {
			throw e;
		}
		
	}
	
	//實做領域檢核(本身aggregate 可進行的檢核)
	public DomainErrorStatus checkTrainNoExist(CreateTrainCommand command) {
		// 取得本次交易 Aggregate
		Optional<Train> train = Optional.ofNullable(trainRepository.findByNumber(command.getTrain_no()));
		if (train.isPresent())
			return DomainErrorStatus.TRAIN_NO_EXISTS;
			//throw new ValidateFailedException(DomainErrorStatus.TRAIN_NO_EXISTS,DomainErrorStatus.TRAIN_NO_EXISTS);
		return null;
	}
	
	//實做領域檢核(本身aggregate 可進行的檢核)
	public DomainErrorStatus checkTrainKindValid(CreateTrainCommand command) {
		// 取得本次交易 Aggregate
		
		if(TrainKind.fromLabel(command.getTrain_kind())==null)
		{
			return DomainErrorStatus.TRAIN_KIND_INVALID;
		}
		return null;
	}
	//實做領域檢核(本身aggregate 可進行的檢核)
	public DomainErrorStatus checkTrainStopDuplicate(CreateTrainCommand command) {
		// 取得本次交易 Aggregate
		Map<String, Integer> counter = new HashMap<>();
		for(int i =0 ;i<command.getStops().size();i++) {
			String stop_name = command.getStops().get(i).get("stop_name");
			if(counter.containsKey(stop_name)) {
				//counter.put(stop_name,counter.get(stop_name)+1);
				return DomainErrorStatus.TRAIN_STOP_DUPLICATE;
			}
			else {
				counter.put(stop_name,1);
			}
		}
		return null;

	}
	public Train checkTrainNoExist(CreateTicketCommand command) throws ValidateFailedException {
		// 跨Aggregate
		Optional<Train> train = Optional.ofNullable(trainRepository.findByNumber(command.getTrain_no()));
		if (train.isPresent()) {
			return train.get();
		}	//throw new ValidateFailedException(DomainErrorStatus.TRAIN_NO_EXISTS,DomainErrorStatus.TRAIN_NO_EXISTS);
		else {
			throw new ValidateFailedException(DomainErrorStatus.TRAIN_NO_NOT_EXISTS);
		}
		
	}
	
	public void checkStopValid(CreateTicketCommand command,Train train)throws ValidateFailedException{
		
		//check is From_stop equal to getTo_stop
		if(command.getFrom_stop().equals(command.getTo_stop()))
		{
			throw new ValidateFailedException(DomainErrorStatus.TICKET_STOP_VALID);
		}	
		
		//check order
		List<TrainStop> trainStops=  train.getStops();
		LocalTime From_stopTime=null;
		LocalTime To_stopTime=null;
		for(int i =0 ;i <trainStops.size();i++) {   
			if(trainStops.get(i).getName().equals(command.getFrom_stop())) {
				From_stopTime = trainStops.get(i).getTime();
			}
			else if(trainStops.get(i).getName().equals(command.getTo_stop())) {
				To_stopTime = trainStops.get(i).getTime();
			}
		}
		if(From_stopTime == null || To_stopTime==null)
		{
			throw new ValidateFailedException(DomainErrorStatus.TICKET_STOP_VALID);
		}
		if(From_stopTime.compareTo(To_stopTime)>0) {
			throw new ValidateFailedException(DomainErrorStatus.TICKET_STOP_VALID);
		}
		
	}
	
}
