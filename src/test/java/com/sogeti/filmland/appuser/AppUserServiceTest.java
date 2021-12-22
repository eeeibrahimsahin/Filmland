package com.sogeti.filmland.appuser;

import com.sogeti.filmland.exceptions.UsernameNotFoundException;
import com.sogeti.filmland.registration.RegistrationResponse;
import com.sogeti.filmland.security.hash.PasswordHashingService;
import com.sogeti.filmland.security.authorisationkey.AuthorisationKeyService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AppUserService.class})
@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @MockBean
    private AuthorisationKeyService authorisationKeyService;
    @MockBean
    private PasswordHashingService passwordHashingService;
    @Mock
    private AppUserJpaRepository appUserJpaRepository;
    @InjectMocks
    private AppUserService underTest;
    private AppUser appUser;
    private RegistrationResponse registrationResponse;

    @BeforeEach
    void setUp() {
        appUser = AppUser
                .builder()
                .email("ibra@gmail.com")
                .password("12345")
                .build();
    }

    // Tests fo save()
    @Test
    void canSave() {
        //Arrange
        //Act
        underTest.save(appUser);
        //Assert
        verify(appUserJpaRepository).save(appUser);
    }

    @Test
    void willThrowWhenPassIllegalStateSave() {
        //Arrange
        when(this.appUserJpaRepository.save((AppUser) any()))
                .thenThrow(new IllegalStateException("Heyoo"));

        //Act
        assertThrows(IllegalStateException.class, () -> this.underTest.save(appUser));

        //Assert
        verify(this.appUserJpaRepository).save((AppUser) any());
    }


    //Test for loadUserByEmail()
    @Test
    void canLoadUserByEmail() {
        //Arrange
        Mockito.when(appUserJpaRepository.findByEmail(any(String.class))).thenReturn(Optional.of(appUser));
        //Act
        AppUser user = underTest.loadUserByEmail("ibra@gmail.com");
        //Assert
        assertThat(user).isEqualTo(appUser);
    }

    @Test
    void willThrowWhenUserNotFound() {
        //Arrange
        when(this.appUserJpaRepository.findByEmail((String) any()))
                .thenReturn(Optional.empty());
        //Act
        assertThrows(UsernameNotFoundException.class, () -> this.underTest.loadUserByEmail("alex@gmail.com"));

        //Assert
        verify(this.appUserJpaRepository).findByEmail((String) any());
    }

    @Test
    void willThrowWhenPassIllegalState() {
        //Arrange
        when(this.appUserJpaRepository.findByEmail((String) any()))
                .thenThrow(new IllegalStateException("Heyooo"));
        //Act
        assertThrows(IllegalStateException.class, () -> this.underTest.loadUserByEmail("alex@gmail.com"));

        //Assert
        verify(this.appUserJpaRepository).findByEmail((String) any());
    }


//TODO: Test for signUpUser()

}