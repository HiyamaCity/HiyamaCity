package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import de.hiyamacity.items.HiyamaCityItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Setter
@Getter
public class Shop {

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

    @Expose
    private String name;
    @Expose
    private UUID[] owner;
    @Expose
    private ArrayList<? extends HiyamaCityItem> items;
    @Expose
    private ShopType[] shopType;
    @Expose
    private Location location;

    public Shop(String name, UUID[] owner, ArrayList<? extends HiyamaCityItem> items, ShopType[] shopType, Location location) {
        this.name = name;
        this.owner = owner;
        this.items = items;
        this.shopType = shopType;
        this.location = location;
    }

    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
    }

    public static Shop fromJson(String string) {
        return new GsonBuilder().create().fromJson(string, Shop.class);
    }

}