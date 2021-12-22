package com.sogeti.filmland.payment;

import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.subscription.Subscription;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public void paySubscriptionFee(AppUser user, Subscription subscription) {
        //TODO: improve method logic
        System.out.println(String.format("Pay %s euro for %s",
                subscription.getSubscriptionType().getName()
                , subscription.getSubscriptionPrice()));
    }
}
