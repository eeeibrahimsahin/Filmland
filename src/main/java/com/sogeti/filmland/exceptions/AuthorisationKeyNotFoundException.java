package com.sogeti.filmland.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AuthorisationKeyNotFoundException extends RuntimeException {
    public AuthorisationKeyNotFoundException(String message) {
        super(message);
    }
}
