package de.hiyamacity.database;

import de.hiyamacity.objects.User;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySqlPointer {

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
}