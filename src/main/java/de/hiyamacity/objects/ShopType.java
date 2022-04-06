package de.hiyamacity.objects;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ShopType {

    FOOD("food"),
    DRINK("drink"),
    AGRICULTURE("agriculture"),
    SOUVENIRS("souvenirs"),
    ORES("ores"),
    ARMOR("armor"),
    PET("pet"),
    TIMBER("timber"),
    STONES("stones"),
    NETHER("nether"),
    END("end"),
    DECORATION("decoration"),
    WORKINGBLOCKS("workingblocks"),
    WEAPONS("weapons");

    ShopType(String type) {

    }

    public static List<String> getAllTypes() {
        return Stream.of(ShopType.values())
                .map(ShopType::name)
                .collect(Collectors.toList());
    }

}
