package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Resident {

    public enum ResidentType {
        OWNER(),
        RENTER()
    }

    @Expose
    private UUID uuid;
    @Expose
    private ResidentType residentType;

    public Resident(UUID uuid, ResidentType renterType) {
        this.uuid = uuid;
        this.residentType = renterType;
    }

    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
    }

    public static Resident fromJson(String string) {
        return new GsonBuilder().create().fromJson(string, Resident.class);
    }

}