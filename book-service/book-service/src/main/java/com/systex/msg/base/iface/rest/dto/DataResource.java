package com.systex.msg.base.iface.rest.dto;

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
public class DataResource {

	private String uuid;
	private String name;

}
