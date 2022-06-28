package com.systex.msg.book.domain.book.command;

import com.systex.msg.base.domain.command.EventIdempotent;
import com.systex.msg.base.domain.share.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookCommand extends EventIdempotent {

	private String name;
	private String author;
	private String isbn;
	private UUID coupon;
	
	private UUID couponNo;

}
