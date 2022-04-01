package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import de.hiyamacity.database.ConnectionPool;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class House {

    private UUID houseID;
    private Location[] doorLocations;
    private Resident[] renters;
    private Address address;

    public House(UUID owner, Location[] doorLocations, Address address) {
        this.houseID = generateNonOccupiedUUID();
        this.doorLocations = doorLocations;
        this.address = address;
        this.renters = new Resident[]{new Resident(owner, Resident.RenterType.OWNER)};
    }

    private UUID generateNonOccupiedUUID() {
        UUID uuid = UUID.randomUUID();
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM HOUSES WHERE ID = ?")) {
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) generateNonOccupiedUUID();
                return uuid;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    public static House fromJson(String string) {
        return new GsonBuilder().create().fromJson(string, House.class);
    }
}