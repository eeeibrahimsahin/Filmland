package com.sogeti.filmland.category.payload.responsemodels;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CategoryResponse {
    private final List<AvailableCategoryResponse> availableCategories;
    private final List<SubscribedCategoryResponse> subscribedCategories;
}
