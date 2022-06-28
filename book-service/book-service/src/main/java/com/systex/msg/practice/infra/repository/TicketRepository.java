package com.systex.msg.practice.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.practice.domain.ticket.aggregate.Ticket;

/**
 * Repository class for the Aggregate
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

	Ticket findByTicketNo(UUID ticketNo);
}
