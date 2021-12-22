package com.sogeti.filmland.subscription;

import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.appuser.AppUserService;
import com.sogeti.filmland.category.Category;
import com.sogeti.filmland.category.CategoryService;
import com.sogeti.filmland.exceptions.AuthorisationKeyNotValidException;
import com.sogeti.filmland.security.authorisationkey.AuthorisationKeyService;
import com.sogeti.filmland.subscription.payload.requestmodels.SubscriptionRequest;
import com.sogeti.filmland.subscription.payload.requestmodels.SubscriptionSharingRequest;
import com.sogeti.filmland.subscription.payload.responsemodels.SubscriptionResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sogeti.filmland.constants.Constant.*;
import static com.sogeti.filmland.constants.Constant.Path.PATH_SHARE;
import static com.sogeti.filmland.constants.Constant.Path.PATH_SUBSCRIPTION;

@RestController
@AllArgsConstructor
@RequestMapping(PATH_SUBSCRIPTION)
public class SubscriptionController {
    private final AppUserService appUserService;
    private final SubscriptionService subscriptionService;
    private final CategoryService categoryService;
    private final AuthorisationKeyService authorisationKeyService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscriptionForUser(
            @RequestBody SubscriptionRequest request,
            @RequestHeader(name = "Authorisation-key") String header) throws AuthorisationKeyNotValidException {

        String status = FAILED;
        String message = SUBSCRIPTION_ALREADY_EXISTS;

        AppUser user = appUserService.loadUserByEmail(request.getEmail());

        if (!authorisationKeyService.checkKey(header, user)) {
            //TODO: Error handling
            throw new AuthorisationKeyNotValidException(
                    String.format(KEY_NOT_VALID, header, user.getEmail()));
        }

        Category category = categoryService.loadCategoryByName(request.getAvailableCategory());

        if (subscriptionService.createSubscriptionForUser(user, category)) {
            status = SUCCESSFUL;
            message = SUBSCRIPTION_SUCCESSFUL + request.getAvailableCategory();
        }
        return ResponseEntity.ok().body(
                SubscriptionResponse
                        .builder()
                        .status(status)
                        .message(message)
                        .build()
        );
    }

    @PostMapping(PATH_SHARE)
    public ResponseEntity<SubscriptionResponse> shareSubscription(
            @RequestBody SubscriptionSharingRequest request,
            @RequestHeader(name = "Authorisation-key") String header) {
        AppUser user = appUserService.loadUserByEmail(request.getEmail());
        if (!authorisationKeyService.checkKey(header, user)) {
            //TODO: ERROR handling
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(SubscriptionResponse.builder().status(LOGIN_FAILED).message(LOGIN_SUCCESSFUL).build());
        }
        return ResponseEntity.ok().body(subscriptionService.shareSubscription(request));
    }
}
