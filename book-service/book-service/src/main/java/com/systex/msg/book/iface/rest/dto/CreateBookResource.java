package com.systex.msg.book.iface.rest.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resource class for the Command API
 */
@Data
@NoArgsConstructor
public class CreateBookResource {

	private String bookId;

	@NotBlank
	private String name;
	@NotBlank
	private String author;
	@NotBlank
	private String isbn;
	@NotBlank
	private String label;
	
	private String coupon;

}
