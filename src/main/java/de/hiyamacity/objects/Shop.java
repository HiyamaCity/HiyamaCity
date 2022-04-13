package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import de.hiyamacity.items.HiyamaCityItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.UUID;

@Setter
@Getter
public class Shop {
    @Expose
    private String name;
    @Expose
    private UUID owner;
    @Expose
    private ArrayList<? extends HiyamaCityItem> items;
    @Expose
    private ShopType[] shopType;
    @Expose
    private Location location;

    public Shop(String name, UUID owner, ArrayList<? extends HiyamaCityItem> items, ShopType[] shopType, Location location) {
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