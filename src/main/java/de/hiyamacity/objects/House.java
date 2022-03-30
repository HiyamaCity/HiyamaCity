package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.UUID;

@Getter
@Setter
public class House {

    private UUID uuid;
    private Location[] doorLocations;
    private UUID owner;
    private UUID[] renters;
    private String address;
    private long postalCode;
    private long houseNumber;

    public House(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    public static House fromJson(String string) {
        return new GsonBuilder().create().fromJson(string, House.class);
    }
}
