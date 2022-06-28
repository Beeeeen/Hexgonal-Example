package com.systex.msg.config;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.systex.msg.exception.BadRequestException;
import com.systex.msg.exception.ErrorResponse;
import com.systex.msg.exception.NotFoundException;
import com.systex.msg.exception.ValidateFailedException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class RestExceptionHandler {

	/**
	 * 處理資源找不到異常
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(NotFoundException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleNofFound(RuntimeException e) {
		ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadRequestException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleDuplicateName(RuntimeException e) {
		ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Controller 檢核失敗異常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleArgumentNotValid(ConstraintViolationException e) {
		ErrorResponse errorResponse = new ErrorResponse(e);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Controller 檢核失敗異常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleArgumentNotValid(MethodArgumentNotValidException e) {
		ErrorResponse errorResponse = new ErrorResponse(e);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Controller 檢核失敗異常
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleArgumentNotValid(MissingServletRequestParameterException e) {
		ErrorResponse errorResponse = new ErrorResponse(e);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Domain 檢核失敗異常
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ValidateFailedException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleDomainCheckFailed(ValidateFailedException e) {
		ErrorResponse errorResponse = new ErrorResponse(e);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * 處理系統異常
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	public ResponseEntity<Object> handleException(HttpRequestMethodNotSupportedException e) {
		log.error("error", e);
		ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * 處理系統異常
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<Object> handleException(Exception e) {
		log.error("error", e);
		ErrorResponse errorResponse = new ErrorResponse("System Error");
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
