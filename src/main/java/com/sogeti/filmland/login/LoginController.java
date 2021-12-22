package com.sogeti.filmland.login;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sogeti.filmland.constants.Constant.Path.PATH_LOGIN;

@RestController
@RequestMapping(PATH_LOGIN)
@AllArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok().body(loginService.attemptLogin(request));
    }
}
