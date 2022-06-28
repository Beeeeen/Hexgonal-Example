package com.systex.msg.book.domain.book.outbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCreatedEventData {

	private String bookId;
	private String couponNo;

}
