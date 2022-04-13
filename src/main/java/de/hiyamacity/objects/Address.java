package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import de.hiyamacity.main.Main;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {

    private String street;
    private long postalCode;
    private String city;
    private long houseNumber;

    public Address(String street, long houseNumber, String city, long postalCode) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return new GsonBuilder().serializeNulls().create().toJson(this);
    }

    public String getAsAddress() {
        return this.getStreet() + ", " + this.getHouseNumber() + " " + this.getPostalCode() + ", " + this.getCity();
    }

    public static Address fromJson(String string) {
        return new GsonBuilder().create().fromJson(string, Address.class);
    }
}