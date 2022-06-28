package com.systex.msg.base.iface.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.systex.msg.base.domain.command.FooSampleCommand;
import com.systex.msg.base.iface.rest.dto.FooSampleResource;
import com.systex.msg.base.iface.rest.dto.StatusResource;
import com.systex.msg.base.service.SampleCommandService;
import com.systex.msg.exception.ValidateFailedException;
import com.systex.msg.util.BaseDataTransformer;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Validated
@RequestMapping("/sample")
@RestController
@AllArgsConstructor
@Tag(name = "Sample API", description = "Sample API")
public class SampleController extends BaseController {

	@Autowired
	SampleCommandService commandService; // Application Service Dependency

	@PostMapping("/foo")
	public StatusResource foo() throws ValidateFailedException {

		// for demo
		FooSampleResource resource = new FooSampleResource();
		resource.setFoo("hello");
		
		// 防腐處理
		FooSampleCommand command = BaseDataTransformer.transformDTO(resource, FooSampleCommand.class);
		
		// 呼叫 Application Service
		commandService.foo(command);
		
		// 回傳結果
		return new StatusResource("OK");
	}

}
