package com.sogeti.filmland.login;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private final String status;
    private final String message;
}
