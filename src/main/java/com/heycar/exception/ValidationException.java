package com.heycar.exception;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {

    private final List<String> validationErrors;

    public ValidationException(List<String> validationErrors) {
        super();
        this.validationErrors = Collections.unmodifiableList(validationErrors);
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    @Override
    public String getMessage() {
    	return validationErrors.stream().collect(Collectors.joining(", "));
    }

}
