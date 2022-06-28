package com.systex.msg.base.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systex.msg.base.domain.sample.aggregate.Sample;

/**
 * Repository class for the Aggregate
 */
@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {

}
