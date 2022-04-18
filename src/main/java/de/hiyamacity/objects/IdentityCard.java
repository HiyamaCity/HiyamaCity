package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class IdentityCard {

    @Expose
    private String forename;
    @Expose
    private String lastname;
    @Expose
    private Color favoriteColor;
    @Expose
    private LocalDate dayOfBirth;
    @Expose
    private List<Address> residentialAddresses;

    public IdentityCard(String forename, String lastname, LocalDate dayOfBirth) {
        this.forename = forename;
        this.lastname = lastname;
        this.dayOfBirth = dayOfBirth;
    }

    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
    }

    public IdentityCard fromJson(String string) {
        return new GsonBuilder().create().fromJson(string, IdentityCard.class);
    }
}
