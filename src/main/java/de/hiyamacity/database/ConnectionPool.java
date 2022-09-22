package de.hiyamacity.database;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionPool {
	private static final File configFile = new File("plugins/Script", "mysql.yml");
	private static final FileConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);
	private static final String host = cfg.getString("host");
	private static final String port = cfg.getString("port");
	private static final String database = cfg.getString("database");
	private static final String username = cfg.getString("username");
	private static final String password = cfg.getString("password");
	private static HikariDataSource dataSource;

	public static void initDatabases() {
		try (Connection con = getDataSource().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS PLAYERS (UUID VARCHAR(40), PLAYER JSON)")) {
				ps.executeUpdate();
			}
			try (PreparedStatement ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS HOUSES (UUID VARCHAR(40), HOUSE JSON)")) {
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void initDatabaseConnectionPool() {
		dataSource = new HikariDataSource();
		dataSource.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useJDBCCompliantTimezoneShift=true&&serverTimezone=Europe/Berlin&&useUnicode=true&autoReconnect=true");
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
		cfg.addDefault("host", "db");
		cfg.addDefault("port", 3306);
		cfg.addDefault("database", "mc");
		cfg.addDefault("username", "root");
		cfg.addDefault("password", "password");
		try {
			cfg.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}