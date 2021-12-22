package com.sogeti.filmland.registration;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sogeti.filmland.constants.Constant.Path.PATH_REGISTER;

@RestController
@RequestMapping(PATH_REGISTER)
@AllArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok().body(registrationService.register(request));
    }

}