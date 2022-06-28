package com.systex.msg.book.iface.event;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.systex.msg.base.iface.event.BaseEventHandler;
import com.systex.msg.book.domain.book.outbound.BookCreatedEvent;
import com.systex.msg.book.domain.book.outbound.BookCreatedEventData;
import com.systex.msg.book.domain.coupon.command.UseCouponCommand;
import com.systex.msg.book.infra.broker.CouponEventSource;
import com.systex.msg.book.service.CouponCommandService;
import com.systex.msg.util.BaseDataTransformer;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@EnableBinding(CouponEventSource.class)
@AllArgsConstructor
public class CouponEventHandler extends BaseEventHandler {

	CouponCommandService service; // Application Service Dependency
	
	static {
		BaseDataTransformer.addConverter(BookCreatedEvent.getConverter(), BookCreatedEvent.class, UseCouponCommand.class);
	}


	@StreamListener(target = CouponEventSource.Topic.COUPON_USING_SUB)
	public void receiveEvent(BookCreatedEvent event) {
		BookCreatedEventData eventData = event.getData();
		log.info("[EVENT] receive event, data: {}", eventData);

		// 防腐處理
		UseCouponCommand command = BaseDataTransformer.transformDTO(event, UseCouponCommand.class);

		if (command.getCouponNo() != null) {
			// 呼叫 Application Service
			service.use(command);
		}
	}

}
