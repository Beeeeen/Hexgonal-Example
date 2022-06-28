package com.systex.msg.practice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.systex.msg.base.service.BaseApplicationService;
import com.systex.msg.exception.NotFoundException;
import com.systex.msg.practice.domain.train.aggregate.Train;
import com.systex.msg.practice.domain.train.command.QueryStopsCommand;
import com.systex.msg.practice.domain.train.command.QueryTrainCommand;
import com.systex.msg.practice.infra.repository.TrainRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class TrainQueryService extends BaseApplicationService {

	private TrainRepository repository; // Inject Dependencies

	/**
	 * Find the stops of the trainNo
	 * 
	 * @param trainNo
	 * @return
	 */
	public Train query(QueryStopsCommand command) {
		Optional<Train> train = Optional.ofNullable(repository.findByNumber(command.getTrainNo()));
		if (train.isPresent())
			return train.get();
		else
			throw new NotFoundException("車次不存在");
	}
	/**
	 * Find the train that pass through the via
	 * 
	 * @param via
	 * @return
	 */
	public List<Train> query(QueryTrainCommand command) {
		List<Train> train = repository.findByStopsNameOrderByStopsTimeAsc(command.getVia());
		return train;
		
	}
	
	
	

}