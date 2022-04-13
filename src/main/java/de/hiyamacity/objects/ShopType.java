package de.hiyamacity.objects;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ShopType {

    FOOD(),
    DRINK(),
    AGRICULTURE(),
    SOUVENIRS(),
    ORES(),
    ARMOR(),
    PET(),
    TIMBER(),
    STONES(),
    NETHER(),
    END(),
    DECORATION(),
    BLOCKS(),
    WEAPONS();

    ShopType() {

    }

    public static List<String> getAllTypes() {
        return Stream.of(ShopType.values())
                .map(ShopType::name)
                .collect(Collectors.toList());
    }

}
