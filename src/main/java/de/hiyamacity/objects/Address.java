package de.hiyamacity.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {

    private String address;
    private long postalCode;
    private long houseNumber;

    public Address(String address, long postalCode, long houseNumber) {
        this.address = address;
        this.postalCode = postalCode;
        this.houseNumber = houseNumber;
    }

}