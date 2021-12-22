package com.sogeti.filmland.subscription;

import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionJpaRepository extends JpaRepository<Subscription,Long> {
    List<Subscription> findAllByUserId(Long id);

    List<Subscription> findAllByUserIdOrSharedWith_Id(Long id, Long id2);

    boolean existsByUserAndSubscriptionType(AppUser user, Category category);

    Optional<Subscription> findByUserAndSubscriptionType_Name(AppUser appUser, String categoryName);
}
