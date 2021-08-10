package com.heycar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.heycar.exception.ValidationException;

@ControllerAdvice()
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

	private static final Logger log = LogManager.getLogger(ControllerAdvisor.class);

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ServiceError> handleValidationException(ValidationException exception) {
		log.error("handleValidationException - BAD DEALER INPUT", exception);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ServiceError.from(exception));
	}

	public static class ServiceError {

		public ServiceError(String errorCode, String errorMessage) {
			this.errorCode = errorCode;
			this.errorMessage = errorMessage;
		}

		private String errorCode;
		private String errorMessage;
		private Object details;

		public static ServiceError from(ValidationException validationException) {
			return new ServiceError(validationException.getMessage(), HttpStatus.BAD_REQUEST.toString());
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public String getErrorCode() {
			return errorCode;
		}

		public Object getDetails() {
			return details;
		}

	}

}
