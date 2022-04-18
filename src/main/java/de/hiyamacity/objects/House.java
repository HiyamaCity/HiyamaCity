package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import de.hiyamacity.database.ConnectionPool;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class House {

    @Expose
    private UUID houseID = generateNonOccupiedUUID();
    @Expose
    private DoorLocation[] doorLocations;
    @Expose
    private Resident[] residents;
    @Expose
    private Address address;

    /**
     * Instantiates a new House object and registers it in the Database.
     *
     * @param owner         UUID of the House Owner.
     * @param doorLocations DoorLocation Array containing the coordinates for the locations of the doors.
     * @param address       Address containing the address for the new House.
     */
    public House(UUID owner, DoorLocation[] doorLocations, Address address) {
        this.doorLocations = doorLocations;
        this.address = address;
        this.residents = new Resident[]{new Resident(owner, Resident.ResidentType.OWNER)};
        registerHouse();
    }

    /**
     * @return Returns a UUID that is not occupied in the Database
     */
    private static UUID generateNonOccupiedUUID() {
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
        return null;
    }


    /**
     * Registers a new House in the Database.
     */
    private void registerHouse() {
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO HOUSES (UUID, HOUSE) VALUES (?,?)")) {
                ps.setString(1, this.getHouseID().toString());
                ps.setString(2, this.toString());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param houseID houseID queried in the Database to return the corresponding House object.
     * @return Returns a House object corresponding to its houseID.
     */
    public House getHouse(String houseID) {
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

    /**
     * @param owner UUID of the Houses' owner queried in the Database to return the corresponding House object.
     * @return Returns a House object corresponding to its houseID.
     */
    public House getHouse(UUID owner) {
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