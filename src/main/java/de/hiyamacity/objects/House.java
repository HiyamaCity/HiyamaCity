package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import de.hiyamacity.database.ConnectionPool;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class House {

    @Expose
    private UUID houseID;
    @Expose
    private DoorLocation[] doorLocations;
    @Expose
    private Resident[] residents;
    @Expose
    private Address address;

    public House(UUID owner, UUID houseID, DoorLocation[] doorLocations, Address address) {
        this.houseID = houseID;
        this.doorLocations = doorLocations;
        this.address = address;
        this.residents = new Resident[]{new Resident(owner, Resident.ResidentType.OWNER)};
    }

    public static @NotNull UUID generateNonOccupiedUUID() {
        UUID uuid = UUID.randomUUID();
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM HOUSES WHERE UUID = ?")) {
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) generateNonOccupiedUUID();
                return uuid;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generateNonOccupiedUUID();
    }

    public static void registerHouse(House house) {
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO HOUSES (UUID, HOUSE) VALUES (?,?)")) {
                UUID uuid = House.generateNonOccupiedUUID();
                ps.setString(1, uuid.toString());
                ps.setString(2, house.toString());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static House getHouse(String houseID) {
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT HOUSE FROM HOUSES WHERE UUID = ?")) {
                ps.setString(1, houseID);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return House.fromJson(rs.getString("HOUSE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static House getHouse(UUID owner) {
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT HOUSE FROM HOUSES WHERE JSON_EXTRACT(HOUSE, \"$.uuid\") = ?")) {
                ps.setString(1, owner.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return House.fromJson(rs.getString("HOUSE"));
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
    }

    public static House fromJson(String string) {
        return new GsonBuilder().create().fromJson(string, House.class);
    }
}