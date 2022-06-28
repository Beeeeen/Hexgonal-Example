package com.systex.msg.book.domain.book.command;

import com.systex.msg.base.domain.command.EventIdempotent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseBookCommand extends EventIdempotent {

	private String bookId;

}
