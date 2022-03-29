package de.hiyamacity.database;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionPool {

    private static HikariDataSource dataSource;
    private static final FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/Script", "mysql.yml"));
    private static final String host = cfg.getString("host");
    private static final String port = cfg.getString("port");
    private static final String database = cfg.getString("database");
    private static final String username = cfg.getString("username");
    private static final String password = cfg.getString("password");

    public static void initDatabases() throws SQLException {
        try (Connection con = getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS PLAYERDATA (UUID VARCHAR(40), PLAYER JSON)")) {
                ps.executeUpdate();
            }
            try(PreparedStatement ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS LANGUAGE (UUID VARCHAR(40), COUNTRY VARCHAR(255), LANG VARCHAR(255))")){
             ps.executeUpdate();
            }
        }
    }

    public static void initDatabaseConnectionPool() {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useJDBCCompliantTimezoneShift=true&&serverTimezone=UTC&&useUnicode=true&autoReconnect=true");
        dataSource.setUsername(username);
        dataSource.setPassword(password);
    }

    public static void closeDatabaseConnectionPool() {
        dataSource.close();
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }

    public static void writeFile() {
        cfg.options().copyDefaults(true);
        cfg.addDefault("host", "localhost");
        cfg.addDefault("port", 3306);
        cfg.addDefault("database", "mc");
        cfg.addDefault("username", "root");
        cfg.addDefault("password", "");
    }

}