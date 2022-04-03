package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Resident {

    public enum RenterType {
        OWNER(),
        RENTER()
    }

    private UUID uuid;
    private RenterType renterType;

    public Resident(UUID uuid, RenterType renterType) {
        this.uuid = uuid;
        this.renterType = renterType;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    public static Resident fromJson(String string) {
        return new GsonBuilder().create().fromJson(string, Resident.class);
    }

}