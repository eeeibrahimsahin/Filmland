package com.sogeti.filmland.appuser;

import com.sogeti.filmland.subscription.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserJpaRepository extends JpaRepository<AppUser,Long> {
    Optional<AppUser> findByEmail(String email);

}
