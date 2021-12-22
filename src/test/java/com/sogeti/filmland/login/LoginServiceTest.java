package com.sogeti.filmland.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.appuser.AppUserService;
import com.sogeti.filmland.exceptions.UsernameNotFoundException;
import com.sogeti.filmland.security.hash.PasswordHashingService;
import com.sogeti.filmland.security.authorisationkey.AuthorisationKeyService;

import java.util.ArrayList;

import com.sogeti.filmland.utilities.LoginChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {LoginService.class})
@ExtendWith(SpringExtension.class)
class LoginServiceTest {
    @MockBean
    private AppUserService appUserService;

    @MockBean
    private AuthorisationKeyService authorisationKeyService;

    @MockBean
    private LoginChecker loginChecker;

    @Autowired
    private LoginService loginService;

    @MockBean
    private PasswordHashingService passwordHashingService;

    private AppUser appUser;
    @BeforeEach
    void setUp() {
        appUser = AppUser
                .builder()
                .email("ibra@gmail.com")
                .password("12345")
                .subscriptionList(new ArrayList<>())
                .build();
    }
    @Test
    void testAttemptLogin() throws UsernameNotFoundException {
        //Arrange
        when(this.passwordHashingService.hashPassword((String) any())).thenReturn("12345");
        when(this.loginChecker.test((AppUser) any(), (LoginRequest) any())).thenReturn(true);
        when(this.authorisationKeyService.createAuthorisationKey((AppUser) any())).thenReturn("Ibrahim");
        when(this.appUserService.loadUserByEmail((String) any())).thenReturn(appUser);

        //Act
        LoginResponse actualAttemptLoginResult = this.loginService
                .attemptLogin(new LoginRequest("ibra@gmail.com", "12345"));
        //Assert
        assertEquals("Authorisation key is Ibrahim", actualAttemptLoginResult.getMessage());
        assertEquals("Login Successful", actualAttemptLoginResult.getStatus());
        verify(this.passwordHashingService).hashPassword((String) any());
        verify(this.loginChecker).test((AppUser) any(), (LoginRequest) any());
        verify(this.authorisationKeyService).createAuthorisationKey((AppUser) any());
        verify(this.appUserService).loadUserByEmail((String) any());
    }

    @Test
    void testAttemptLogin2() throws UsernameNotFoundException {
        //Arrange
        when(this.passwordHashingService.hashPassword((String) any())).thenReturn("12345");
        when(this.loginChecker.test((AppUser) any(), (LoginRequest) any())).thenReturn(false);
        when(this.authorisationKeyService.createAuthorisationKey((AppUser) any())).thenReturn("Ibrahim");
        when(this.appUserService.loadUserByEmail((String) any())).thenReturn(appUser);
        //Act
        LoginResponse actualAttemptLoginResult = this.loginService
                .attemptLogin(new LoginRequest("ibra@gmail.com", "12345"));
        //Assert
        assertEquals("Username or password is wrong", actualAttemptLoginResult.getMessage());
        assertEquals("Login Failed", actualAttemptLoginResult.getStatus());
    }

}

