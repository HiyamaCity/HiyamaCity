package de.hiyamacity.objects;

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

}