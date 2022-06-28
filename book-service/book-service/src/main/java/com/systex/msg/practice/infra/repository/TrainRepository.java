package com.systex.msg.practice.infra.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systex.msg.practice.domain.train.aggregate.Train;


/**
 * Repository class for the Aggregate
 */
@Repository
public interface TrainRepository extends JpaRepository<Train, Integer> {

	//Optional<Train> findBynumber(Integer trainNo);
	Train findByNumber(Integer trainNo);
	
	List<Train> findByStopsNameOrderByStopsTimeAsc(String name);
	
	

}
