package com.systex.msg.book.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.book.domain.book.aggregate.Book;

/**
 * Repository class for the Aggregate
 */
@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

}
