package com.sogeti.filmland.registration;

import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class RegistrationRequest {
    private final String email;
    private final String password;
}
