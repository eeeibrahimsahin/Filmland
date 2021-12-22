package com.sogeti.filmland.subscription.payload.responsemodels;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubscriptionResponse {
    private final String status;
    private final String message;
}
