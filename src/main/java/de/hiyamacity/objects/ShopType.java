package de.hiyamacity.objects;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ShopType {

    FOOD("food"),
    DRINK("drink"),
    AGRICULTURE("agriculture"),
    SOUVENIERS("souveniers"),
    ORES("ores"),
    ARMOR("armor"),
    PET("pet"),
    TIMBER("timber"),
    WEAPONS("weapons");

    ShopType(String type) {

    }

    public static List<String> getAllTypes() {
        return Stream.of(ShopType.values())
                .map(ShopType::name)
                .collect(Collectors.toList());
    }

}
