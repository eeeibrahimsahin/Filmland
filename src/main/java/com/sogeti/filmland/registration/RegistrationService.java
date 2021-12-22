package com.sogeti.filmland.registration;

import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.appuser.AppUserService;
import com.sogeti.filmland.utilities.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sogeti.filmland.constants.Constant.EMAIL_NOT_VALID;
import static com.sogeti.filmland.constants.Constant.MONTHLY_LIMIT;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final AppUserService appUserService;
    private final EmailValidator emailValidator;

    public RegistrationResponse register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) throw new IllegalStateException(EMAIL_NOT_VALID);
        return appUserService.singUpUser(
                AppUser.builder()
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .monthlyLimit(MONTHLY_LIMIT)
                        .build()
        );
    }

}
