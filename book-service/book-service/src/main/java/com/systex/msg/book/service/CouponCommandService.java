package com.systex.msg.book.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.systex.msg.base.domain.AggregateProxyFactory;
import com.systex.msg.base.service.BaseApplicationService;
import com.systex.msg.book.domain.coupon.aggregate.Coupon;
import com.systex.msg.book.domain.coupon.command.UseCouponCommand;
import com.systex.msg.book.infra.repository.CouponRepository;
import com.systex.msg.exception.NotFoundException;

import lombok.AllArgsConstructor;

/**
 * Application Service class for the Commands
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
@AllArgsConstructor
public class CouponCommandService extends BaseApplicationService {

	private CouponRepository repository;

	public Coupon use(UseCouponCommand command) {
		
		// 取得本次交易 Aggregate
		Coupon coupon = repository.findByCouponNo(command.getCouponNo());
		if (coupon == null)
			throw new NotFoundException(String.format("Coupon is invalid. (%s)", command.getCouponNo().getValue()));
		
		// 叫用 Command Handler
		Coupon proxy = AggregateProxyFactory.getProxyInstance(Coupon.class, coupon);
		proxy.use(command);
		
		// 儲存 Aggregate
		repository.save(coupon);
		
		return coupon;
	}

}
