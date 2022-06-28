package com.systex.msg.book.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.book.domain.coupon.aggregate.Coupon;

/**
 * Repository class for the Aggregate
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

	Coupon findByToken(UUID token);
	Coupon findByCouponNo(UUID couponNo);
}
