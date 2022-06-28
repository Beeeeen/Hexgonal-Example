package com.systex.msg.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ValidateFailedException extends Exception {

	private static final long serialVersionUID = -2294054198084347502L;

	@Getter
	private final List<DomainErrorStatus> errors = new ArrayList<>();

	@Getter
	private final List<ParamedError> paramedErrors = new ArrayList<>();

	public ValidateFailedException(DomainErrorType domainErrorType) {
		super(domainErrorType.getType());
	}

	public ValidateFailedException(DomainErrorStatus... errors) {
		super(DomainErrorType.VALIDATE_FAILED.name());
		if (errors != null && errors.length > 0)
			this.errors.addAll(Arrays.asList(errors));
	}

	public ValidateFailedException(DomainErrorStatus error, String msg) {
		super(DomainErrorType.VALIDATE_FAILED.name());
		error.setMessage(msg);
		errors.add(error);
	}

	public void add(DomainErrorStatus error) {
		errors.add(error);
	}

	public void add(DomainErrorStatus error, String msg) {
		error.setMessage(msg);
		errors.add(error);
	}

	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	public void add(ParamedError paramedError) {
		paramedErrors.add(paramedError);
	}

	public boolean hasParamedErrors() {
		return !paramedErrors.isEmpty();
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum DomainErrorType {
		
		VALIDATE_FAILED("validation-error", "Validation Failed"), 
		USER_CONFIRM("user-confirm", "User Confirm"),
		;

		@Getter
		private final String type;

		@Getter
		private String description;

		DomainErrorType(String type, String description) {
			this.type = type;
			this.description = description;
		}
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum DomainErrorStatus {

		NOT_FOUND("NotFound", "Not Found"), 
		VALIDATE_FAILED("ValidateFailed", "Validate Failed"),
		
		SAMPLE_SOMETHING_WRONG("SomethingWrong", "Something Wrong"),
		
		BOOK_VERSION_INVALID("BookVersionInvalid", "Book Version is invalid"),
		COUPON_INVALID("CouponInvalid", "Coupon is invalid"),
		
		TRAIN_NO_EXISTS("TrainNoExists","Train No is exists"),
		TRAIN_KIND_INVALID("TrainKindInvalid","Train Kind is invalid"),
		TRAIN_STOP_DUPLICATE("TrainStopsDuplicate","Train Stops is duplicate"),
		TRAIN_STOP_SORTED("TrainStopsNotSorted","Train Stops is not sorted"),
		TICKET_STOP_VALID("TicketStopsInvalid","Ticket From & To is invalid"),
		TRAIN_NOT_AVALIABLE("TrainNotAvailable","Train is not available"),
		TRAIN_NO_NOT_EXISTS("TrainNoNotExists","Train No does not exists")
		;

		@Getter
		private final String code;

		@Getter
		@Setter
		private String message;

		DomainErrorStatus(String code, String message) {
			this.code = code;
			this.message = message;
		}
	}

	// 支援參數的 error 格式
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class ParamedError {
		String code;
		List<String> params;
	}

}
