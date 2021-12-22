package com.sogeti.filmland.login;

import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.appuser.AppUserService;
import com.sogeti.filmland.security.hash.PasswordHashingService;
import com.sogeti.filmland.security.authorisationkey.AuthorisationKeyService;
import com.sogeti.filmland.utilities.LoginChecker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sogeti.filmland.constants.Constant.*;


@Service
@AllArgsConstructor
public class LoginService {
    private final AppUserService appUserService;
    private final LoginChecker loginChecker;
    private final AuthorisationKeyService authorisationKeyService;
    private final PasswordHashingService passwordHashingService;

    public LoginResponse attemptLogin(final LoginRequest request) {
        String status = LOGIN_FAILED;
        String message = USERNAME_OR_PASSWORD_IS_WRONG;
        AppUser appUser = appUserService.loadUserByEmail(request.getEmail());

        if (loginChecker.test(appUser, hashRequestPassword(request))) {
            status = LOGIN_SUCCESSFUL;
            message = AUTHORISATION_KEY_IS + authorisationKeyService.createAuthorisationKey(appUser);
        }
        return LoginResponse
                .builder()
                .status(status)
                .message(message)
                .build();
    }

    private LoginRequest hashRequestPassword(LoginRequest request) {
        return LoginRequest.
                builder()
                .email(request.getEmail())
                .password(passwordHashingService.hashPassword(request.getPassword()))
                .build();
    }
}
