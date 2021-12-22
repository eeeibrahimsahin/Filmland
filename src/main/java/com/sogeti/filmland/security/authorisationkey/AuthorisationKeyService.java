package com.sogeti.filmland.security.authorisationkey;

import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.exceptions.AuthorisationKeyNotFoundException;
import com.sogeti.filmland.exceptions.AuthorisationKeyNotValidException;
import com.sogeti.filmland.utilities.KeyValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.sogeti.filmland.constants.Constant.KEY_NOT_FOUND;

@Service
@AllArgsConstructor
public class AuthorisationKeyService {

    private final AuthorisationKeyJpaRepository authorisationKeyJpaRepository;
    private final KeyValidator keyValidator;

    public void saveAuthorisationKey(AuthorisationKey authorisationKey) {
        authorisationKeyJpaRepository.save(authorisationKey);
    }

    public Optional<AuthorisationKey> getKey(String key) {
        return authorisationKeyJpaRepository.findByKey(key);
    }

    public String createAuthorisationKey(AppUser appUser) {
        String key = UUID.randomUUID().toString();
        AuthorisationKey authorisationKey = AuthorisationKey.builder()
                .key(key)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(30))
                .user(appUser)
                .build();
        saveAuthorisationKey(authorisationKey);

        return authorisationKey.getKey();
    }

    public boolean checkKey(String key, AppUser appUser) {
        AuthorisationKey authorisationKey = getKey(key)
                .orElseThrow(() -> new AuthorisationKeyNotFoundException(
                        String.format(KEY_NOT_FOUND, key)));
        if (!authorisationKey.getUser().equals(appUser)) return false;
        return keyValidator.test(authorisationKey.getExpiredAt());

    }
}
