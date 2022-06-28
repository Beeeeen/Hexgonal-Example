package com.systex.msg.practice.iface.rest.dto;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTrainResource {
	
	@NotNull(message= "車號不可為空")
	@Range(min = 1,message= "車號需為正整數")
	private Integer train_no;
	@NotBlank(message = "車種不可為空")
	private String train_kind;
	@NotEmpty(message = "停靠站不可為空")
	private List<Map<String, String>> stops;
	
}
