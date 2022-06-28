package com.systex.msg.base.iface.rest.dto;

import java.util.List;

import lombok.Data;

/**
 * Resource class for the Command API
 */
@Data
public class PageableResource<T> {

	private PageResource page;
	private List<T> content;

}
