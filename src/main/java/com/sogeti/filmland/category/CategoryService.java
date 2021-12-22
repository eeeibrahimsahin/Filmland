package com.sogeti.filmland.category;

import com.sogeti.filmland.appuser.AppUserService;
import com.sogeti.filmland.category.payload.responsemodels.AvailableCategoryResponse;
import com.sogeti.filmland.category.payload.responsemodels.SubscribedCategoryResponse;
import com.sogeti.filmland.exceptions.CategoryNotFoundException;
import com.sogeti.filmland.exceptions.UsernameNotFoundException;
import com.sogeti.filmland.subscription.Subscription;
import com.sogeti.filmland.subscription.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.sogeti.filmland.constants.Constant.CATEGORY_NOT_FOUND;


@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryJpaRepository categoryJpaRepository;
    private final AppUserService appUserService;
    private final SubscriptionService subscriptionService;


    public Category loadCategoryByName(String name) {
        return categoryJpaRepository
                .findByName(name)
                .orElseThrow(() ->
                        new CategoryNotFoundException(String.format(CATEGORY_NOT_FOUND, name)));
    }

    public void saveCategories() {
        for (CategoryType categoryType : CategoryType.values()) {
            if (categoryJpaRepository.findByName(categoryType.name).isEmpty()) {
                Category category = Category
                        .builder()
                        .name(categoryType.name)
                        .availableContent(categoryType.availableContent)
                        .price(categoryType.price)
                        .build();
                categoryJpaRepository.save(category);
            }
        }
    }

    public List<Category> getAvailableCategories(String email) {
        List<Subscription> subscriptions = subscriptionService
                .findAllByUserId(appUserService.loadUserByEmail(email).getId());
        if (subscriptions.isEmpty()) return categoryJpaRepository.findAll();

        List<String> categoryList = subscriptions
                .stream()
                .map(subscription -> subscription.getSubscriptionType().getName())
                .collect(Collectors.toList());

        return categoryJpaRepository.findAllByNameNotIn(categoryList);
    }

    public List<Category> getSubscribedCategories(String email) {
        List<Subscription> subscriptions = subscriptionService
                .findAllByUserId(appUserService.loadUserByEmail(email).getId());

        return subscriptions
                .stream()
                .map(Subscription::getSubscriptionType)
                .collect(Collectors.toList());
    }

    public List<AvailableCategoryResponse> availableCategoryInResponseModel(String email) {
        List<Category> availableCategories = getAvailableCategories(email);

        return availableCategories.stream().map(
                        category ->
                                AvailableCategoryResponse
                                        .builder()
                                        .name(category.getName())
                                        .availableContent(category.getAvailableContent())
                                        .price(category.getPrice())
                                        .build()
                )
                .collect(Collectors.toList());
    }

    public List<SubscribedCategoryResponse> subscribedCategoryInResponseModel(String email) {
        List<Subscription> subscriptions = subscriptionService
                .findAllByUserId(appUserService.loadUserByEmail(email).getId());

        return subscriptions.stream().map(
                        subscription -> SubscribedCategoryResponse
                                .builder()
                                .name(subscription.getSubscriptionType().getName())
                                .remainingContent(subscription.getSubscriptionType().getAvailableContent())
                                .price(subscription.getSubscriptionPrice())
                                .startDate(subscription.getStartDate())
                                .build()
                )
                .collect(Collectors.toList());
    }
}

