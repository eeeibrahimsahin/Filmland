package com.sogeti.filmland.subscription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.appuser.AppUserService;
import com.sogeti.filmland.category.Category;
import com.sogeti.filmland.exceptions.SubscriptionNotFoundException;
import com.sogeti.filmland.exceptions.UsernameNotFoundException;
import com.sogeti.filmland.payment.PaymentService;
import com.sogeti.filmland.scheduler.CustomSchedulerService;
import com.sogeti.filmland.subscription.payload.requestmodels.SubscriptionSharingRequest;
import com.sogeti.filmland.subscription.payload.responsemodels.SubscriptionResponse;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {SubscriptionService.class})
@ExtendWith(SpringExtension.class)
class SubscriptionServiceTest {
    @MockBean
    private CustomSchedulerService customSchedulerService;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private SubscriptionService subscriptionService;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private SubscriptionJpaRepository subscriptionJpaRepository;

    @Autowired
    private SubscriptionService underTest;
    private AppUser appUser;
    private Category category;

    @BeforeEach
    void setUp() {
        appUser = AppUser
                .builder()
                .email("ibra@gmail.com")
                .password("12345")
                .monthlyLimit(50)
                .subscriptionList(new ArrayList<>())
                .build();
        category = Category
                .builder()
                .name("Dutch Films")
                .availableContent(10)
                .price(4.0)
                .build();
    }

    @Test
    void testFindNotSharedSubscriptionByUserId() {
        ArrayList<Subscription> subscriptionList = new ArrayList<>();
        when(this.subscriptionJpaRepository.findAllByUserId((Long) any())).thenReturn(subscriptionList);
        List<Subscription> actualFindNotSharedSubscriptionByUserIdResult = this.underTest
                .findNotSharedSubscriptionByUserId(123L);
        assertSame(subscriptionList, actualFindNotSharedSubscriptionByUserIdResult);
        assertTrue(actualFindNotSharedSubscriptionByUserIdResult.isEmpty());
        verify(this.subscriptionJpaRepository).findAllByUserId((Long) any());
    }


    @Test
    void testFindNotSharedSubscriptionByUserIdWhenSubscriptionNotFound() {
        when(this.subscriptionJpaRepository.findAllByUserId((Long) any()))
                .thenThrow(new SubscriptionNotFoundException("Format"));
        assertThrows(SubscriptionNotFoundException.class,
                () -> this.underTest.findNotSharedSubscriptionByUserId(123L));
    }

    @Test
    void testFindAllByUserId() {
        ArrayList<Subscription> subscriptionList = new ArrayList<>();
        when(this.subscriptionJpaRepository.findAllByUserIdOrSharedWith_Id((Long) any(), (Long) any()))
                .thenReturn(subscriptionList);
        List<Subscription> actualFindAllByUserIdResult = this.underTest.findAllByUserId(123L);
        assertSame(subscriptionList, actualFindAllByUserIdResult);
        assertTrue(actualFindAllByUserIdResult.isEmpty());
        verify(this.subscriptionJpaRepository).findAllByUserIdOrSharedWith_Id((Long) any(), (Long) any());
    }


    @Test
    void testFindAllByUserIdWhenSubscriptionNotFound() {
        when(this.subscriptionJpaRepository.findAllByUserIdOrSharedWith_Id((Long) any(), (Long) any()))
                .thenThrow(new SubscriptionNotFoundException("Format"));
        assertThrows(SubscriptionNotFoundException.class, () -> this.underTest.findAllByUserId(123L));
    }

    @Test
    void testCreateSubscriptionForUser() {
        when(this.subscriptionJpaRepository.existsByUserAndSubscriptionType((AppUser) any(), (Category) any()))
                .thenReturn(true);

        assertFalse(this.underTest.createSubscriptionForUser(appUser, category));
    }


    @Test
    void testCreateSubscriptionForUserWhenThrowError() {
        when(this.subscriptionJpaRepository.existsByUserAndSubscriptionType((AppUser) any(), (Category) any()))
                .thenThrow(new SubscriptionNotFoundException("Format"));
        assertThrows(SubscriptionNotFoundException.class,
                () -> this.underTest.createSubscriptionForUser(appUser, category));
    }

    @Test
    void testIsAlreadySubscribed() {
        when(this.subscriptionJpaRepository.existsByUserAndSubscriptionType((AppUser) any(), (Category) any()))
                .thenReturn(true);
        assertTrue(this.underTest.isAlreadySubscribed(appUser, category));
    }


    @Test
    void testIsAlreadySubscribedWhenThrowError() {
        when(this.subscriptionJpaRepository.existsByUserAndSubscriptionType((AppUser) any(), (Category) any()))
                .thenThrow(new SubscriptionNotFoundException("kkk"));
        assertThrows(SubscriptionNotFoundException.class,
                () -> this.underTest.isAlreadySubscribed(appUser, category));
    }

    @Test
    void testIsAlreadySubscribedWhenNotFound() {
        when(this.subscriptionJpaRepository.existsByUserAndSubscriptionType((AppUser) any(), (Category) any()))
                .thenReturn(false);
        assertFalse(this.underTest.isAlreadySubscribed(appUser, category));
    }



}

