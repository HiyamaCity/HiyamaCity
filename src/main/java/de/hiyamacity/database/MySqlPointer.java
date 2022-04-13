package de.hiyamacity.database;

import de.hiyamacity.objects.Address;
import de.hiyamacity.objects.House;
import de.hiyamacity.objects.User;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySqlPointer {

    /**
     * @param uuid Unique user ID of the Player.
     * @return Return if the User is present in the Database or not.
     */
    public static boolean isUserExist(UUID uuid) {
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM PLAYERS WHERE UUID = ?")) {
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Registers a new User Object and its corresponding UUID in the Database.
     *
     * @param uuid Unique user ID of the Player.
     * @param user New Instance of the User class.
     */
    public static void registerUser(UUID uuid, User user) {
        if (isUserExist(uuid)) return;
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO PLAYERS (UUID, PLAYER) VALUES (?,?)")) {
                ps.setString(1, uuid.toString());
                ps.setString(2, user.toString());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a known user to a new User Object. Used for Object manipulation.
     *
     * @param uuid Unique user ID of the Player.
     * @param user Updates the User Object in the Database.
     */
    public static void updateUser(UUID uuid, User user) {
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("UPDATE PLAYERS SET PLAYER = ? WHERE UUID = ?")) {
                ps.setString(1, user.toString());
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param uuid Unique user ID of the Player.
     * @return Returns a User Object from the Database by its corresponding UUID.
     */
    public static @NotNull User getUser(UUID uuid) {
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT PLAYER FROM PLAYERS WHERE UUID = ?")) {
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return User.fromJson(rs.getString("PLAYER"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new User();
    }

    public static void registerHouse(UUID owner, House house) {
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
}