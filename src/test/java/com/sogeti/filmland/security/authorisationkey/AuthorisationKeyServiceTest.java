package com.sogeti.filmland.security.authorisationkey;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.exceptions.AuthorisationKeyNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import com.sogeti.filmland.utilities.KeyValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthorisationKeyService.class})
@ExtendWith(SpringExtension.class)
class AuthorisationKeyServiceTest {
    @Autowired
    private AuthorisationKeyService underTest;
    @MockBean
    private AuthorisationKeyJpaRepository authorisationKeyJpaRepository;
    @MockBean
    private KeyValidator keyValidator;
    private AppUser appUser;
    private AuthorisationKey authorisationKey;

    @BeforeEach
    void setUp() {
        appUser = AppUser
                .builder()
                .email("ibra@gmail.com")
                .password("12345")
                .subscriptionList(new ArrayList<>())
                .build();
        authorisationKey = AuthorisationKey
                .builder()
                .user(appUser)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(30))
                .key("12345")
                .build();
    }

    @Test
    void testSaveAuthorisationKey() {
        //Arrange
        when(this.authorisationKeyJpaRepository.save((AuthorisationKey) any())).thenReturn(authorisationKey);
        //Act
        this.underTest.saveAuthorisationKey(authorisationKey);
        //Assert
        verify(this.authorisationKeyJpaRepository).save((AuthorisationKey) any());
    }

    @Test
    void willThrowWhenKeyNotFound() {
        //Arrange
        when(this.authorisationKeyJpaRepository.save((AuthorisationKey) any()))
                .thenThrow(new AuthorisationKeyNotFoundException("Key"));
        //Act-Assert
        assertThrows(AuthorisationKeyNotFoundException.class,
                () -> this.underTest.saveAuthorisationKey(authorisationKey));
    }

    @Test
    void testGetKey() {
        //Arrange
        Optional<AuthorisationKey> ofResult = Optional.of(authorisationKey);
        when(this.authorisationKeyJpaRepository.findByKey((String) any())).thenReturn(ofResult);
        //Act
        Optional<AuthorisationKey> actualKey = this.underTest.getKey("Key");
        //Assert
        assertSame(ofResult, actualKey);
        assertTrue(actualKey.isPresent());
        verify(this.authorisationKeyJpaRepository).findByKey((String) any());
    }

    @Test
    void willThrowWhenKeyNotFound2() {
        //Arrange
        when(this.authorisationKeyJpaRepository.findByKey((String) any()))
                .thenThrow(new AuthorisationKeyNotFoundException("Key"));
        //Act-Assert
        assertThrows(AuthorisationKeyNotFoundException.class, () -> this.underTest.getKey("Key"));
    }

    @Test
    void testCreateAuthorisationKey() {
        //Arrange
        when(this.authorisationKeyJpaRepository.save((AuthorisationKey) any())).thenReturn(authorisationKey);
        //Act
        this.underTest.createAuthorisationKey(appUser);
        //Assert
        verify(this.authorisationKeyJpaRepository).save((AuthorisationKey) any());
    }

    @Test
    void willThrowWhenKeyNotFound3() {
        //Arrange
        when(this.authorisationKeyJpaRepository.save((AuthorisationKey) any()))
                .thenThrow(new AuthorisationKeyNotFoundException("Format"));
        //Act-Assert
        assertThrows(AuthorisationKeyNotFoundException.class,
                () -> this.underTest.createAuthorisationKey(appUser));
    }

    @Test
    void testCheckKey() {
        //Arrange
        when(this.keyValidator.test((LocalDateTime) any())).thenReturn(true);
        Optional<AuthorisationKey> ofResult = Optional.of(authorisationKey);
        when(this.authorisationKeyJpaRepository.findByKey((String) any())).thenReturn(ofResult);
        //Act-Assert
        assertTrue(this.underTest.checkKey("Key", appUser));
    }

    @Test
    void willThrowWhenKeyNotFound4() {
        //Arrange
        when(this.keyValidator.test((LocalDateTime) any()))
                .thenThrow(new AuthorisationKeyNotFoundException("helllooo"));
        Optional<AuthorisationKey> ofResult = Optional.of(authorisationKey);
        when(this.authorisationKeyJpaRepository.findByKey((String) any())).thenReturn(ofResult);
        //Act-Assert
        assertThrows(AuthorisationKeyNotFoundException.class, () -> this.underTest.checkKey("Key", appUser));

    }

    @Test
    void testCheckKeyWhenKeyValidatorReturnFalse() {
        //Arrange
        when(this.keyValidator.test((LocalDateTime) any())).thenReturn(false);
        Optional<AuthorisationKey> ofResult = Optional.of(authorisationKey);
        when(this.authorisationKeyJpaRepository.findByKey((String) any())).thenReturn(ofResult);
        //Act-Assert
        assertFalse(this.underTest.checkKey("Key", appUser));

    }

}

