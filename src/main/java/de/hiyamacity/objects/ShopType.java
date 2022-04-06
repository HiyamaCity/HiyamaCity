package de.hiyamacity.objects;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ShopType {

    FOOD("food"), DRINK("drink"), AGRICULTURE("agriculture"), SOUVENIRS("souvenirs"), ORES("ores"), ARMOR("armor"), PET("pet"), TIMBER("timber"), WEAPONS("weapons");

    private final String type;

    ShopType(String type) {
        this.type = type;
    }

    public static List<String> getAllTypes() {
        return Stream.of(ShopType.values()).map(ShopType::name).collect(Collectors.toList());
    }

    public ShopType getShopType(String s) {
        return valueOf(s);
    }

    @Override
    public String toString() {
        return this.type;
    }
}
