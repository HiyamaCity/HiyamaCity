package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import de.hiyamacity.main.Main;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class User {

    private long purse;
    private long bank;
    private long playedMinutes;
    private long playedHours;
    private long banStart;
    private long banEnd;
    private long kills;
    private long deaths;
    private boolean isBanned;
    private boolean isConfirmedTeamspeak;
    private String banID;
    private UUID uuid;
    private String tsIdentifier;
    private String banReason;
    private String forename;
    private String surname;

    public User(UUID uuid) {
        this.purse = 4000;
        this.bank = 2000;
        this.playedMinutes = 0;
        this.playedHours = 0;
        this.uuid = uuid;
    }

    public User() {
    }

    @Override
    public String toString() {
        return new GsonBuilder().registerTypeAdapter(User.class, this).create().toJson(this);
    }

    public static User fromJson(String string) {
        return new GsonBuilder().create().fromJson(string, User.class);
    }

}
