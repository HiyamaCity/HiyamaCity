package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import de.hiyamacity.items.HiyamaCityItem;
import de.hiyamacity.main.Main;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.UUID;

@Setter
@Getter
public class Shop {

    private String name;
    private UUID owner;
    private ArrayList<? extends HiyamaCityItem> items;
    private ShopType shopType;
    private Location location;

    public Shop(String name, UUID owner, ArrayList<? extends HiyamaCityItem> items, ShopType shopType, Location location) {
        this.name = name;
        this.owner = owner;
        this.items = items;
        this.shopType = shopType;
        this.location = location;
    }

    @Override
    public String toString() {
        return Main.toJsonString(this);
    }

    public static Shop fromJson(String string) {
        return new GsonBuilder().create().fromJson(string, Shop.class);
    }
}