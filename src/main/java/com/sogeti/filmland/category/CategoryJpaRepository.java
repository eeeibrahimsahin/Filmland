package com.sogeti.filmland.category;

import com.sogeti.filmland.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    List<Category> findAllByNameNotIn(List<String> name);

}
