package com.sogeti.filmland.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class MonthlyLimitExcessException extends RuntimeException {
    public MonthlyLimitExcessException(String message) {
        super(message);
    }
}
