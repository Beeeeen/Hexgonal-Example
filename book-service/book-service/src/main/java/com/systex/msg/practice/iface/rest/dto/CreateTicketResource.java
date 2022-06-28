package com.systex.msg.practice.iface.rest.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTicketResource {

	@NotNull(message= "車號不可為空")
	@Range(min = 1,message= "車號需為正整數")
	private Integer train_no;
	@NotBlank(message = "起始站不可為空")
	private String from_stop;
	@NotBlank(message = "終點站不可為空")
	private String to_stop;
	@NotBlank(message = "日期不可為空")
	@Pattern(regexp = "(19|20|21|22|23|24|25|26)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])",message = "日期格式不正確 yyyy-mm-dd")
	private String take_date;

}
