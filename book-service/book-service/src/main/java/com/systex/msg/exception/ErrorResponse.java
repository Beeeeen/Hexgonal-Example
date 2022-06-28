package com.systex.msg.exception;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.systex.msg.exception.ValidateFailedException.DomainErrorStatus;
import com.systex.msg.exception.ValidateFailedException.ParamedError;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
public class ErrorResponse {

	@Getter
	private String error = "Validate Failed";

	@Getter
	@JsonInclude(Include.NON_NULL)
	private List<Map<String, String>> fieldErrors;

	@Getter
	@JsonInclude(Include.NON_NULL)
	private List<DomainErrorStatus> checkErrors;
	
	@Getter
	@JsonInclude(Include.NON_NULL)
	private List<ParamedError> paramedErrors;

	public ErrorResponse(String error) {
		this.error = error;
	}

	// Controller 檢核失敗異常,寫入fieldErrors
	public ErrorResponse(ConstraintViolationException e) {

		List<Map<String, String>> errors = e.getConstraintViolations().stream().map(violation -> {
			final Collection<Path.Node> propertyPath = new ArrayList<>();
			violation.getPropertyPath().forEach(propertyPath::add);

			final Path.Node parent = propertyPath.stream().skip(1L).findFirst().orElse(null);
			log.info("node:{}", parent);
			
			log.info("violation:{}", violation);

			Map<String, String> errorMap = new LinkedHashMap<>();
			errorMap.put("code", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
			errorMap.put("message", violation.getMessage());
			errorMap.put("field", parent.getName());
			return errorMap;
		}).collect(Collectors.toList());

		this.fieldErrors = errors;
	}

	// Controller 檢核失敗異常,寫入fieldErrors
	public ErrorResponse(MethodArgumentNotValidException e) {
		
		List<Map<String, String>> errors = e.getBindingResult().getFieldErrors().stream().map(fieldError -> {
			Map<String, String> errorMap = new LinkedHashMap<>();
			errorMap.put("code", fieldError.getCode());
			errorMap.put("message", fieldError.getDefaultMessage());
			errorMap.put("field", fieldError.getField());
		    return errorMap;
		}).collect(Collectors.toList());
		
		this.fieldErrors = errors;
	}
	
	// Controller 無填寫必填參數,檢核失敗異常,寫入fieldErrors
	public ErrorResponse(MissingServletRequestParameterException e) {

		List<Map<String, String>> errors = new ArrayList<>();
		Map<String, String> errorMap = new LinkedHashMap<>();
		errorMap.put("message", e.getMessage());
		errorMap.put("field", e.getParameterName());
		errors.add(errorMap);
		
		this.fieldErrors = errors;
	}

	// Domain 檢核失敗異常
	public ErrorResponse(ValidateFailedException e) {
		this.error = e.getMessage();
		if (e.hasErrors()) {			
			this.checkErrors = e.getErrors();
		}
		if (e.hasParamedErrors()) {
			this.paramedErrors = e.getParamedErrors();			
		}
	}
	
}
