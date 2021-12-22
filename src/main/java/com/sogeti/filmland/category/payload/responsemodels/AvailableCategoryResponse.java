package com.sogeti.filmland.category.payload.responsemodels;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AvailableCategoryResponse {
    private final String name;
    private final int availableContent;
    private final double price;

}
