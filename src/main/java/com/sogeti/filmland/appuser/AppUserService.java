package com.sogeti.filmland.appuser;

import com.sogeti.filmland.exceptions.MonthlyLimitExcessException;
import com.sogeti.filmland.exceptions.UsernameNotFoundException;
import com.sogeti.filmland.registration.RegistrationResponse;
import com.sogeti.filmland.scheduler.CustomSchedulerService;
import com.sogeti.filmland.security.hash.PasswordHashingService;
import com.sogeti.filmland.security.authorisationkey.AuthorisationKeyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import static com.sogeti.filmland.constants.Constant.*;

@Service
@AllArgsConstructor
public class AppUserService {
    private final AppUserJpaRepository appUserJpaRepository;
    private final PasswordHashingService passwordHashingService;
    private final AuthorisationKeyService authorisationKeyService;
    private final CustomSchedulerService customSchedulerService;

    public void save(AppUser user) {
        appUserJpaRepository.save(user);
    }

    public AppUser loadUserByEmail(String email) throws UsernameNotFoundException {
        return appUserJpaRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public RegistrationResponse singUpUser(AppUser appUser) {
        boolean userExists = appUserJpaRepository
                .findByEmail(appUser.getEmail())
                .isPresent();
        if (userExists) throw new IllegalStateException(EMAIL_ALREADY_TAKEN);

        String hashedPassword = passwordHashingService
                .hashPassword(appUser.getPassword());

        appUser.setPassword(hashedPassword);

        appUserJpaRepository.save(appUser);
        String authorisationKey = authorisationKeyService.createAuthorisationKey(appUser);

        //When a user register the app then Monthly limit is automatically renewed every month.
        customSchedulerService.computePaymentTime(
                () -> refreshMonthlyLimit(appUser),
                ONE_MONTH,
                ONE_MONTH);

        return RegistrationResponse
                .builder()
                .status(REGISTRATION_SUCCESSFUL)
                .message(AUTHORISATION_KEY_IS + authorisationKey)
                .build();
    }

    public void setNewMonthlyLimit(AppUser user, int request) {

        isMonthlyLimitFinish(user);

        if (user.getMonthlyLimit() - request <= 0) {
            user.setMonthlyLimit(0);
        } else {
            user.setMonthlyLimit(user.getMonthlyLimit() - request);
        }

        appUserJpaRepository.save(user);
    }


    private void isMonthlyLimitFinish(AppUser user) {
        if (user.getMonthlyLimit() == 0)
            throw new MonthlyLimitExcessException(
                    String.format(MONTHLY_LIMIT_EXCESS, user.getEmail()));
    }

    public void refreshMonthlyLimit(AppUser user) {
        user.setMonthlyLimit(MONTHLY_LIMIT);
        //TODO: The logic can be improved. This is just to show that I'm using the scheduler.
        System.out.println(user.getEmail());
        appUserJpaRepository.save(user);
    }
}
