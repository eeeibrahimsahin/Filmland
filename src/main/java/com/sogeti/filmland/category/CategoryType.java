package com.sogeti.filmland.category;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CategoryType {
    DUTCH_FILMS("Dutch Films", 10, 4.0),
    DUTCH_SERIES("Dutch Series", 20, 6.0),
    INTERNATIONAL_FILMS("International Films", 30, 8.0);

    public final String name;
    public final int availableContent;
    public final double price;
}
