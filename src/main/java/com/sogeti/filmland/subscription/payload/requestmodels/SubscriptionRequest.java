package com.sogeti.filmland.subscription.payload.requestmodels;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubscriptionRequest {
    private final String email;
    private final String availableCategory;
}
