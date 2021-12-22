package com.sogeti.filmland.utilities;

import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.login.LoginRequest;
import org.springframework.stereotype.Service;

import java.util.function.BiPredicate;
@Service
public class LoginChecker implements BiPredicate<AppUser, LoginRequest> {

    @Override
    public boolean test(AppUser appUser, LoginRequest request) {
        return appUser.getEmail().equals(request.getEmail()) && appUser.getPassword().equals(request.getPassword());
    }
}
