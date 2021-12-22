package com.sogeti.filmland.appuser;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.sogeti.filmland.constants.Constant.Path.*;
@RestController
@AllArgsConstructor
@RequestMapping(PATH_USER)
public class AppUserController {
    private final AppUserService appUserService;

    @PostMapping("{username}")
    public ResponseEntity<AppUser> getUserData(@PathVariable String username) {
        return ResponseEntity.ok().body(appUserService.loadUserByEmail(username));
    }
}
