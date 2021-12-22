package com.sogeti.filmland.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class AuthorisationKeyNotValidException extends RuntimeException {
    public AuthorisationKeyNotValidException(String message) {
        super(message);
    }
}
