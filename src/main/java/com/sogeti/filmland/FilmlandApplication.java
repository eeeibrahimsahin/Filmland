package com.sogeti.filmland;

import com.sogeti.filmland.appuser.AppUser;
import com.sogeti.filmland.appuser.AppUserService;
import com.sogeti.filmland.category.CategoryService;
import com.sogeti.filmland.registration.RegistrationRequest;
import com.sogeti.filmland.registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@AllArgsConstructor
@EnableScheduling
public class FilmlandApplication implements CommandLineRunner {
    private final CategoryService categoryService;
    private final RegistrationService registrationService;

    public static void main(String[] args) {
        SpringApplication.run(FilmlandApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        categoryService.saveCategories();
        RegistrationRequest sampleUser1 = RegistrationRequest
                .builder()
                .email("ibra@gmail.com")
                .password("12345")
                .build();
        RegistrationRequest sampleUser2 = RegistrationRequest
                .builder()
                .email("johan@gmail.com")
                .password("12345")
                .build();
        registrationService.register(sampleUser1);
        registrationService.register(sampleUser2);
    }
}
