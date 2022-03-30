package de.hiyamacity.util;

import de.hiyamacity.database.ConnectionPool;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

public class LanguageHandler {

    public static @NotNull ResourceBundle getResourceBundle(UUID uuid) {
        try (PreparedStatement ps = ConnectionPool.getDataSource().getConnection().prepareStatement("SELECT * FROM LANGUAGE WHERE UUID = ?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return ResourceBundle.getBundle("languagePack", new Locale(rs.getString("LANG"), rs.getString("COUNTRY")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getResourceBundle();
    }

    public static @NotNull ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("languagePack", new Locale(""));
    }
}
