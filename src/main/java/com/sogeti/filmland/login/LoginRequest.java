package com.sogeti.filmland.login;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {
    private final String email;
    private final String password;
}
