package com.sogeti.filmland.category.payload.responsemodels;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class SubscribedCategoryResponse {
    private final String name;
    private final int remainingContent;
    private final double price;
    private final LocalDate startDate;
}
