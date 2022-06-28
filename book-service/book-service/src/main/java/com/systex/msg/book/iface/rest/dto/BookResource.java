package com.systex.msg.book.iface.rest.dto;

import org.modelmapper.AbstractConverter;

import com.systex.msg.book.domain.book.aggregate.Book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Resource class for the Command API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResource {

	private String bookId;
	private String name;
	private String author;
	private String isbn;

	public static AbstractConverter<Book, BookResource> getConverter() {
		return new AbstractConverter<Book, BookResource>() {
			protected BookResource convert(Book source) {
				if (source == null) {
					return null;
				}

				BookResource target = new BookResource();
				target.setBookId(source.getUuid().getValue());
				target.setName(source.getName());
				target.setAuthor(source.getAuthor());
				target.setIsbn(source.getIsbn());
				return target;
			}
		};
	}
}
