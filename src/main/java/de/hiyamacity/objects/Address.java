package de.hiyamacity.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {

    private String street;
    private long postalCode;
    private long houseNumber;

    public Address(String street, long houseNumber, long postalCode) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
    }

}