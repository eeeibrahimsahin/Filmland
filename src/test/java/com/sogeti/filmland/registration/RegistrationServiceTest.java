package com.sogeti.filmland.registration;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.appuser.AppUserService;
import com.sogeti.filmland.utilities.EmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {RegistrationService.class})
@ExtendWith(SpringExtension.class)
class RegistrationServiceTest {
    @Autowired
    private RegistrationService underTest;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private EmailValidator emailValidator;

    private RegistrationRequest request;
    private RegistrationResponse response;

    @BeforeEach
    void setUp() {
        request = new RegistrationRequest("ibra@gmail.com", "12345");
        response = RegistrationResponse
                .builder()
                .status("Ok")
                .message("Heyooo")
                .build();
    }

    @Test
    void testRegister() {
        //Arrange
        when(this.emailValidator.test((String) any())).thenReturn(true);
        RegistrationResponse registrationResponse = new RegistrationResponse("Ok", "Yes babyy");
        when(this.appUserService.singUpUser((AppUser) any())).thenReturn(registrationResponse);
        //Act - Arrange
        assertSame(registrationResponse,
                this.underTest.register(request));
        verify(this.emailValidator).test((String) any());
        verify(this.appUserService).singUpUser((AppUser) any());
    }

    @Test
    void willThrowWhenPassIllegalState() {
        //Arrange
        when(this.emailValidator.test((String) any())).thenReturn(true);
        when(this.appUserService.singUpUser((AppUser) any()))
                .thenThrow(new IllegalStateException("Heyyyy"));
        //Act-Assert
        assertThrows(IllegalStateException.class,
                () -> this.underTest.register(request));
    }

    @Test
    void willThrowWhenEmailValidatorReturnFalse() {
        //Arrange
        when(this.emailValidator.test((String) any())).thenReturn(false);
        when(this.appUserService.singUpUser((com.sogeti.filmland.appuser.AppUser) any()))
                .thenReturn(response);
        //Act-Assert
        assertThrows(IllegalStateException.class,
                () -> this.underTest.register(request));
    }

}

