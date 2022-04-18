package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class IdentityCard {

    @Expose
    private String forename;
    @Expose
    private String surname;
    @Expose
    private Color favoriteColor;
    @Expose
    private LocalDate dayOfBirth;
    @Expose
    private List<Address> residentialAddresses;

    public IdentityCard(String forename, String surname, LocalDate dayOfBirth) {
        this.forename = forename;
        this.surname = surname;
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
