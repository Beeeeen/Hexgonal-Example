package com.systex.msg.base.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systex.msg.base.domain.eventlog.aggregate.EventLog;
import com.systex.msg.base.domain.share.UUID;

/**
 * Repository class for the Aggregate
 */
@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long> {

	EventLog findByUuid(UUID uuid);

}
