package com.sogeti.filmland.registration;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistrationResponse {
    private final String status;
    private final String message;
}
