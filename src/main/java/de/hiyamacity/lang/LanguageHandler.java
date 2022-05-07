package de.hiyamacity.lang;

import de.hiyamacity.database.ConnectionPool;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

public class LanguageHandler {

    public static Locale[] availableLocales;

    public static @NotNull ResourceBundle getResourceBundle(UUID uuid) {
        try (Connection con = ConnectionPool.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM LANGUAGE WHERE UUID = ?")) {
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                    return ResourceBundle.getBundle("strings",
                            new Locale(rs.getString("LANG"), rs.getString("COUNTRY")), new XMLResourceBundleControl());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getResourceBundle();
    }

    public static @NotNull ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("strings", new Locale("de", "DE"), new XMLResourceBundleControl());
    }
}
