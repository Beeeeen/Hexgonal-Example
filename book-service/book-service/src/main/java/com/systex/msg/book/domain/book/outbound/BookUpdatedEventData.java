package com.systex.msg.book.domain.book.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdatedEventData {

	private String bookId;

}
