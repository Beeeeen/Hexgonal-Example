package com.systex.msg.base.iface.rest.dto;

import lombok.Data;

/**
 * Resource class for the Command API
 */
@Data
public class PageResource {

	private int totalPages;
	private long totalElements;
	private int size;
	private int number;
	private int numberOfElements;
	private boolean last;
	private boolean first;
	private boolean empty;

}
