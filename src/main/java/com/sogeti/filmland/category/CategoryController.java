package com.sogeti.filmland.category;

import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.appuser.AppUserService;
import com.sogeti.filmland.category.payload.responsemodels.CategoryResponse;
import com.sogeti.filmland.exceptions.AuthorisationKeyNotValidException;
import com.sogeti.filmland.security.authorisationkey.AuthorisationKeyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sogeti.filmland.constants.Constant.KEY_NOT_VALID;
import static com.sogeti.filmland.constants.Constant.Path.PATH_CATEGORY;

@RestController
@RequestMapping(PATH_CATEGORY)
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final AuthorisationKeyService authorisationKeyService;
    private final AppUserService appUserService;

    @GetMapping("{username}")
    public ResponseEntity<CategoryResponse> getCategoryInfoOfUser(
            @PathVariable String username,
            @RequestHeader(name = "Authorisation-key") String header) throws AuthorisationKeyNotValidException {

        AppUser user = appUserService.loadUserByEmail(username);

        if (!authorisationKeyService.checkKey(header, user))
            throw new AuthorisationKeyNotValidException(String.format(KEY_NOT_VALID, header,user.getEmail()));

        return ResponseEntity.ok().body(
                CategoryResponse
                        .builder()
                        .availableCategories(categoryService
                                .availableCategoryInResponseModel(username))
                        .subscribedCategories(categoryService
                                .subscribedCategoryInResponseModel(username))
                        .build()
        );
    }
}
