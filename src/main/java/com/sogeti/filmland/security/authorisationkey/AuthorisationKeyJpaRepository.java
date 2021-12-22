package com.sogeti.filmland.security.authorisationkey;

import com.sogeti.filmland.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorisationKeyJpaRepository extends JpaRepository<AuthorisationKey, Long> {

    Optional<AuthorisationKey> findByKey(String key);

    Optional<List<AuthorisationKey>> findAllByUser(AppUser user);
}
