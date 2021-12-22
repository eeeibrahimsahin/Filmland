package com.sogeti.filmland.subscription.payload.requestmodels;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubscriptionSharingRequest {
    private final String email;
    private final String customer;
    private final String subscribedCategory;
}
