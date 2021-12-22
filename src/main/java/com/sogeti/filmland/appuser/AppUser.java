package com.sogeti.filmland.appuser;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sogeti.filmland.security.authorisationkey.AuthorisationKey;
import com.sogeti.filmland.subscription.Subscription;
import lombok.*;

import javax.persistence.*;
import java.util.List;

import static com.sogeti.filmland.constants.Constant.MONTHLY_LIMIT;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //https://stackoverflow.com/questions/11283709/always-use-primitive-object-wrappers-for-jpa-id-instead-of-primitive-type
    private Long id;
    private String email;
    private String password;
    private int monthlyLimit;
    @ManyToMany
    private List<Subscription> subscriptionList;
    @OneToMany
    private List<AuthorisationKey> authorisationKeys;
}
