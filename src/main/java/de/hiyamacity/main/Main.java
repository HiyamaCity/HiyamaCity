package de.hiyamacity.main;

import de.hiyamacity.commands.admin.GameModeCommand;
import de.hiyamacity.commands.user.PayCommand;
import de.hiyamacity.commands.user.PingCommand;
import de.hiyamacity.commands.user.StatsCommand;
import de.hiyamacity.database.ConnectionPool;
import de.hiyamacity.listener.DamageHandler;
import de.hiyamacity.listener.JoinHandler;
import de.hiyamacity.util.PlaytimeTracker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
        ConnectionPool.initDatabases();
        loadCommands();
        loadListeners();
        PlaytimeTracker.startPlaytimeTracker();
    }

    public void onDisable() {
        ConnectionPool.closeDatabaseConnectionPool();
    }

    private void loadCommands() {
        Objects.requireNonNull(getCommand("ping")).setExecutor(new PingCommand());
        Objects.requireNonNull(getCommand("stats")).setExecutor(new StatsCommand());
        Objects.requireNonNull(getCommand("gm")).setExecutor(new GameModeCommand());
        Objects.requireNonNull(getCommand("pay")).setExecutor(new PayCommand());
    }

    private void loadListeners() {
        this.pm.registerEvents(new JoinHandler(), this);
        this.pm.registerEvents(new DamageHandler(), this);
    }

}