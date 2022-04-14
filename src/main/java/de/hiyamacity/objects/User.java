package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class User {

    @Expose
    private long purse;
    @Expose
    private long bank;
    @Expose
    private long playedMinutes;
    @Expose
    private long playedHours;
    @Expose
    private long kills;
    @Expose
    private long deaths;
    @Expose
    private boolean isConfirmedTeamspeak;
    @Expose
    private UUID uuid;
    @Expose
    private String tsIdentifier;
    @Expose
    private String forename;
    @Expose
    private String surname;
    @Expose
    private List<Ban> bans;

    public User(UUID uuid) {
        this.purse = 4000;
        this.bank = 2000;
        this.playedMinutes = 0;
        this.playedHours = 0;
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
    }

    public static User fromJson(String string) {
        return new GsonBuilder().create().fromJson(string, User.class);
    }

}
