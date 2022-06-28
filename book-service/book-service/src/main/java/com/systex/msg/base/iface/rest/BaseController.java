package com.systex.msg.base.iface.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.systex.msg.base.iface.rest.dto.StatusResource;

@RequestMapping("/")
@RestController
public class BaseController {

	@GetMapping("/health")
	public StatusResource health() {
		return new StatusResource("OK");
	}

}
