package com.systex.msg.book.iface.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resource class for the Command API
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookVersionResource {

	private Integer version;

}
