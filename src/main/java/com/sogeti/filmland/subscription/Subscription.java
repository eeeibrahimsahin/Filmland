package com.sogeti.filmland.subscription;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private AppUser user;
    @OneToOne
    private Category subscriptionType;
    private double subscriptionPrice;
    private int remainingContent;
    private LocalDate startDate;
    @OneToOne
    private AppUser sharedWith;
}