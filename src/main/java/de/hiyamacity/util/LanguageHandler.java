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

    public static @NotNull ResourceBundle getResourceBundle(UUID uuid) throws SQLException {
        try (PreparedStatement ps = ConnectionPool.getDataSource().getConnection().prepareStatement("SELECT * FROM LANGUAGE WHERE UUID = ?")) {
            ResultSet rs = ps.executeQuery();
            return ResourceBundle.getBundle("languagePack", new Locale(rs.getString("LANG"), rs.getString("COUNTRY")));
        }
    }

    public static @NotNull ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("languagePack", new Locale(""));
    }
}
