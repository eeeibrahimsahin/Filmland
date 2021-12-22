package com.sogeti.filmland.subscription;

import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.appuser.AppUserService;
import com.sogeti.filmland.category.Category;
import com.sogeti.filmland.exceptions.SubscriptionNotFoundException;
import com.sogeti.filmland.payment.PaymentService;
import com.sogeti.filmland.scheduler.CustomSchedulerService;
import com.sogeti.filmland.subscription.payload.requestmodels.SubscriptionSharingRequest;
import com.sogeti.filmland.subscription.payload.responsemodels.SubscriptionResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.sogeti.filmland.constants.Constant.*;

@Service
@AllArgsConstructor
public class SubscriptionService {
    private final SubscriptionJpaRepository subscriptionJpaRepository;
    private final AppUserService appUserService;
    private final PaymentService paymentService;
    private final CustomSchedulerService customSchedulerService;

    public List<Subscription> findNotSharedSubscriptionByUserId(Long id) {
        return subscriptionJpaRepository.findAllByUserId(id);
    }

    public List<Subscription> findAllByUserId(Long id) {
        return subscriptionJpaRepository.findAllByUserIdOrSharedWith_Id(id, id);
    }

    public boolean createSubscriptionForUser(AppUser user, Category category) {

        if (isAlreadySubscribed(user, category)) return false;
        appUserService.setNewMonthlyLimit(user, category.getAvailableContent());
        Subscription newSubscription = Subscription.builder()
                .user(user)
                .subscriptionType(category)
                .subscriptionPrice(category.getPrice())
                .startDate(LocalDate.now())
                .build();

        subscriptionJpaRepository.save(newSubscription);
        user.getSubscriptionList().add(newSubscription);
        appUserService.save(user);
        //The first month is free; after this month the subscriber pays for the service.
        customSchedulerService.computePaymentTime(
                () -> paymentService.paySubscriptionFee(user, newSubscription),
                ONE_MONTH,
                ONE_MONTH);
        return true;
    }

    public boolean isAlreadySubscribed(AppUser user, Category category) {
        return subscriptionJpaRepository.existsByUserAndSubscriptionType(user, category);
    }


    public SubscriptionResponse shareSubscription(SubscriptionSharingRequest request) {
        AppUser user = appUserService.loadUserByEmail(request.getEmail());
        AppUser userSubscriptionWillBeShared = appUserService.loadUserByEmail(request.getCustomer());

        Subscription subscription = subscriptionJpaRepository
                .findByUserAndSubscriptionType_Name(user, request.getSubscribedCategory())
                .orElseThrow(() ->
                        new SubscriptionNotFoundException(
                                String.format(SUBSCRIPTION_NOT_FOUND,
                                        request.getSubscribedCategory())));

        if (subscription.getSharedWith() != null &&
                subscription.getSharedWith().equals(userSubscriptionWillBeShared))
            return SubscriptionResponse
                    .builder()
                    .status(FAILED)
                    .message(SUBSCRIPTION_ALREADY_SHARED)
                    .build();

        subscription.setSharedWith(userSubscriptionWillBeShared);
        subscription.setSubscriptionPrice(subscription.getSubscriptionPrice() / 2);
        subscription.setRemainingContent(subscription.getRemainingContent() / 2);
        userSubscriptionWillBeShared.getSubscriptionList().add(subscription);

        appUserService.save(userSubscriptionWillBeShared);
        subscriptionJpaRepository.save(subscription);

        return SubscriptionResponse
                .builder()
                .status(SUCCESSFUL)
                .message(SUBSCRIPTION_SHARED_WITH + request.getCustomer())
                .build();
    }

}
