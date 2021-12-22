package com.sogeti.filmland.category;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.appuser.AppUserService;
import com.sogeti.filmland.exceptions.CategoryNotFoundException;
import com.sogeti.filmland.exceptions.UsernameNotFoundException;
import com.sogeti.filmland.subscription.SubscriptionService;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CategoryService.class})
@ExtendWith(SpringExtension.class)
class CategoryServiceTest {

    @Autowired
    private CategoryService underTest;
    @MockBean
    private CategoryJpaRepository categoryJpaRepository;
    @MockBean
    private AppUserService appUserService;
    @MockBean
    private SubscriptionService subscriptionService;
    private Category category;
    private AppUser appUser;

    @BeforeEach
    void setUp() {
        category = Category
                .builder()
                .name("Dutch Films")
                .availableContent(10)
                .price(4.0)
                .build();
        appUser = AppUser
                .builder()
                .email("ibra@gmail.com")
                .password("12345")
                .subscriptionList(new ArrayList<>())
                .build();
    }

    @Test
    void canLoadCategoryByName() {
        //Arrange
        when(this.categoryJpaRepository.findByName((String) any())).thenReturn(Optional.of(category));
        //Act
        assertSame(category, this.underTest.loadCategoryByName("Dutch Films"));
        //Assert
        verify(this.categoryJpaRepository).findByName((String) any());
    }

    @Test
    void willThrowWhenCategoryNotFound() {
        //Arrange
        when(this.categoryJpaRepository.findByName((String) any())).thenThrow(new CategoryNotFoundException("Format"));
        //Act-Assert
        assertThrows(CategoryNotFoundException.class, () -> this.underTest.loadCategoryByName("Name"));
    }

    @Test
    void willThrowWhenCategoryNotFound2() {
        //Arrange
        when(this.categoryJpaRepository.findByName((String) any())).thenReturn(Optional.empty());
        //Act-Assert
        assertThrows(CategoryNotFoundException.class, () -> this.underTest.loadCategoryByName("Name"));
    }


    @Test
    void canGetAvailableCategories() throws UsernameNotFoundException {
        //Arrange
        when(this.subscriptionService.findAllByUserId((Long) any())).thenReturn(new ArrayList<>());
        ArrayList<Category> categoryList = new ArrayList<>();
        when(this.categoryJpaRepository.findAll()).thenReturn(categoryList);
        when(this.appUserService.loadUserByEmail((String) any())).thenReturn(appUser);
        //Act
        List<Category> actualAvailableCategories = this.underTest.getAvailableCategories("ibra@gmail.com");

        //Assert
        assertSame(categoryList, actualAvailableCategories);
        assertTrue(actualAvailableCategories.isEmpty());
        verify(this.subscriptionService).findAllByUserId((Long) any());
        verify(this.categoryJpaRepository).findAll();
        verify(this.appUserService).loadUserByEmail((String) any());
    }

    @Test
    void canGetAvailableCategories2() throws UsernameNotFoundException {
        //Arrange
        when(this.subscriptionService.findAllByUserId((Long) any())).thenReturn(new ArrayList<>());
        when(this.categoryJpaRepository.findAll()).thenThrow(new CategoryNotFoundException("Format"));
        when(this.appUserService.loadUserByEmail((String) any())).thenReturn(appUser);
        //Act - Assert
        assertThrows(CategoryNotFoundException.class,
                () -> this.underTest.getAvailableCategories("ibra@gmail.com"));
        verify(this.subscriptionService).findAllByUserId((Long) any());
        verify(this.categoryJpaRepository).findAll();
        verify(this.appUserService).loadUserByEmail((String) any());
    }

    @Test
    void canGetSubscribedCategories() throws UsernameNotFoundException {
        //Arrange
        when(this.subscriptionService.findAllByUserId((Long) any())).thenReturn(new ArrayList<>());
        when(this.appUserService.loadUserByEmail((String) any())).thenReturn(appUser);
        //Act-Assert
        assertTrue(this.underTest.getSubscribedCategories("ibra@gmail.com").isEmpty());

    }

    @Test
    void willThrowWhenCategoryNotFound3() throws UsernameNotFoundException {
        //Arrange
        when(this.subscriptionService.findAllByUserId((Long) any())).thenThrow(new CategoryNotFoundException("Helloooo"));
        when(this.appUserService.loadUserByEmail((String) any())).thenReturn(appUser);
        //Act-Assert
        assertThrows(CategoryNotFoundException.class,
                () -> this.underTest.getSubscribedCategories("ibra@gmail.com"));

    }


    @Test
    void testAvailableCategoryInResponseModel() throws UsernameNotFoundException {
        //Arrange
        when(this.subscriptionService.findAllByUserId((Long) any())).thenReturn(new ArrayList<>());
        when(this.categoryJpaRepository.findAll()).thenReturn(new ArrayList<>());
        when(this.appUserService.loadUserByEmail((String) any())).thenReturn(appUser);
        //Act-Assert
        assertTrue(this.underTest.availableCategoryInResponseModel("ibra@gmail.com").isEmpty());
    }

    @Test
    void testSubscribedCategoryInResponseModel() throws UsernameNotFoundException {
        //Arrange
        when(this.subscriptionService.findAllByUserId((Long) any())).thenReturn(new ArrayList<>());
        when(this.appUserService.loadUserByEmail((String) any())).thenReturn(appUser);
        //Act-Assert
        assertTrue(this.underTest.subscribedCategoryInResponseModel("ibra@gmail.com").isEmpty());
    }

}

