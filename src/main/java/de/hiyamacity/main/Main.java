package de.hiyamacity.main;

import de.hiyamacity.commands.user.PingCommand;
import de.hiyamacity.database.ConnectionPool;
import de.hiyamacity.listener.JoinHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Objects;

public class Main extends JavaPlugin {

    PluginManager pm = Bukkit.getPluginManager();
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        ConnectionPool.writeFile();
        ConnectionPool.initDatabaseConnectionPool();
        loadCommands();
        loadListeners();
        try {
            ConnectionPool.initDatabases();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onDisable() {
        ConnectionPool.closeDatabaseConnectionPool();
    }

    private void loadCommands() {
        Objects.requireNonNull(getCommand("ping")).setExecutor(new PingCommand());
    }

    private void loadListeners() {
        this.pm.registerEvents(new JoinHandler(), this);
    }

}