package com.systex.msg.book.domain.service;

import org.springframework.stereotype.Service;

import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.book.domain.coupon.aggregate.Coupon;
import com.systex.msg.book.infra.repository.CouponRepository;
import com.systex.msg.exception.ValidateFailedException;
import com.systex.msg.exception.ValidateFailedException.DomainErrorStatus;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookService {

	private CouponRepository couponRepository;
	
	public boolean checkCouponUsed(UUID token) throws ValidateFailedException {
		if (token != null) {
			Coupon coupon = couponRepository.findByToken(token);
			if (coupon == null || coupon.getUsedTo() != null)
				throw new ValidateFailedException(DomainErrorStatus.COUPON_INVALID);
		}
		return true;
	}
	
	public UUID getCouponNo(UUID token) {
		if (token == null)
			return null;
		Coupon coupon = couponRepository.findByToken(token);
		return coupon != null? coupon.getCouponNo(): null;
	}
}
