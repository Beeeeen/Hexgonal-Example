package com.systex.msg.base.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.systex.msg.base.domain.idempotentlog.aggregate.EventIdempotentLog;
import com.systex.msg.base.domain.idempotentlog.aggregate.EventIdempotentLogId;

@Repository
public interface EventIdempotentLogRepository extends JpaRepository<EventIdempotentLog, EventIdempotentLogId>{
	
	@Modifying
	@Query(value="INSERT INTO event_idempotent_log (event_type, unique_key) VALUES (:eventType, :uniqueKey)", nativeQuery = true)
	int insert(String eventType, String uniqueKey);

	@Modifying
	@Query(value="INSERT INTO event_idempotent_log (event_type, unique_key, target_id) VALUES (:eventType, :uniqueKey, :targetId)", nativeQuery = true)
	int insert(String eventType, String uniqueKey, String targetId);
	
	@Modifying
	@Query(value="DELETE FROM event_idempotent_log WHERE event_type = :eventType AND target_id= :targetId", nativeQuery = true)
	int delete(String eventType, String targetId);
	
	EventIdempotentLog findOneByEventTypeAndUniqueKey(String eventType, String uniqueKey);

}
