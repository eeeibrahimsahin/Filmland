package com.sogeti.filmland.utilities;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Predicate;
@Service
public class KeyValidator implements Predicate<LocalDateTime> {

    @Override
    public boolean test(LocalDateTime localDateTime) {
        return localDateTime.isAfter(LocalDateTime.now());
    }
}
