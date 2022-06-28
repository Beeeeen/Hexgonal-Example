package com.systex.msg.book.domain.book.outbound;

import org.modelmapper.AbstractConverter;

import com.systex.msg.base.domain.outbound.BaseEvent;
import com.systex.msg.base.domain.share.UUID;
import com.systex.msg.book.domain.coupon.command.UseCouponCommand;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class BookCreatedEvent extends BaseEvent {

	@Getter
	private BookCreatedEventData data;

	public static AbstractConverter<BookCreatedEvent, UseCouponCommand> getConverter() {
		return new AbstractConverter<BookCreatedEvent, UseCouponCommand>() {
			protected UseCouponCommand convert(BookCreatedEvent source) {
				if (source == null) {
					return null;
				}

				UseCouponCommand target = new UseCouponCommand();
				if (source.getTargetId() != null)
					target.setUsedTo(new UUID(source.getTargetId()));
				if (source.getData().getCouponNo() != null)
					target.setCouponNo(new UUID(source.getData().getCouponNo()));
				return target;
			}
		};
	}
}
