package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {

    @Expose
    private String street;
    @Expose
    private long postalCode;
    @Expose
    private String city;
    @Expose
    private long houseNumber;

    public Address(String street, long houseNumber, String city, long postalCode) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
    }

    public String getAsAddress() {
        return this.getStreet() + ", " + this.getHouseNumber() + " " + this.getPostalCode() + ", " + this.getCity();
    }

    public static Address fromJson(String string) {
        return new GsonBuilder().create().fromJson(string, Address.class);
    }
}