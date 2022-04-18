package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import de.hiyamacity.database.ConnectionPool;
import de.hiyamacity.misc.Distances;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class House {

    @Expose
    private UUID houseID = generateNonOccupiedUUID();
    @Expose
    private List<DoorLocation> doorLocations;
    @Expose
    private List<Resident> residents;
    @Expose
    private Address address;

    /**
     * Instantiates a new House object and registers it in the Database.
     *
     * @param owner         UUID of the House Owner.
     * @param doorLocations DoorLocation Array containing the coordinates for the locations of the doors.
     * @param address       Address containing the address for the new House.
     */
    public House(UUID owner, List<DoorLocation> doorLocations, Address address) {
        this.doorLocations = doorLocations;
        this.address = address;
        this.residents = new ArrayList<>();
        this.residents.add(new Resident(owner, Resident.ResidentType.OWNER));
        registerHouse();
    }

    public static boolean isLockedDoor(Location loc) {
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT JSON_EXTRACT(HOUSE, \"$.doorLocations[*].world\"), JSON_EXTRACT(HOUSE, \"$.doorLocations[*].x\"), JSON_EXTRACT(HOUSE, \"$.doorLocations[*].y\"), JSON_EXTRACT(HOUSE, \"$.doorLocations[*].z\") FROM HOUSES")) {
                System.out.println(ps);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String world = rs.getString("JSON_EXTRACT(HOUSE, \"$.doorLocations[*].world\")").replace("[", "").replace("]", "").replace("\"", "");
                    String xString = rs.getString("JSON_EXTRACT(HOUSE, \"$.doorLocations[*].x\")").replace("[", "").replace("]", "");
                    String yString = rs.getString("JSON_EXTRACT(HOUSE, \"$.doorLocations[*].y\")").replace("[", "").replace("]", "");
                    String zString = rs.getString("JSON_EXTRACT(HOUSE, \"$.doorLocations[*].z\")").replace("[", "").replace("]", "");
                    Location doorLocation = new Location(Bukkit.getWorld(world), Double.parseDouble(xString), Double.parseDouble(yString), Double.parseDouble(zString));
                    if (loc.distanceSquared(doorLocation) <= Distances.HOUSE_DOOR_INTERACTION_MARGIN) return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean allowedToOpen(UUID uuid, Location loc) {
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            List<House> houses = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM HOUSES WHERE JSON_EXTRACT(HOUSE, \"$.residents[*].uuid\") = ?")) {
                ps.setString(1, "[\"" + uuid.toString() + "\"]");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    houses.add(House.fromJson(rs.getString("HOUSE")));
                }
            }
            for (House house : houses) {
                for (DoorLocation doorLocation : house.getDoorLocations()) {
                    if (new Location(Bukkit.getWorld(doorLocation.getWorld()), doorLocation.getX(), doorLocation.getY(), doorLocation.getZ()).distanceSquared(loc) <= Distances.HOUSE_DOOR_INTERACTION_MARGIN)
                        return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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