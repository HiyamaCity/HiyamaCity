package de.hiyamacity.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.hiyamacity.commands.admin.GameModeCommand;
import de.hiyamacity.commands.admin.HouseCommand;
import de.hiyamacity.commands.admin.VanishCommand;
import de.hiyamacity.commands.user.*;
import de.hiyamacity.database.ConnectionPool;
import de.hiyamacity.listener.*;
import de.hiyamacity.util.PlaytimeTracker;
import de.hiyamacity.util.RankHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {

    final PluginManager pm = Bukkit.getPluginManager();
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    private static final Gson gson = new GsonBuilder().create();
    

    public void onEnable() {
        instance = this;
        ConnectionPool.writeFile();
        ConnectionPool.initDatabaseConnectionPool();
        ConnectionPool.initDatabases();
        PlaytimeTracker.startPlaytimeTracker();
        RankHandler.initScoreboard();

        loadCommands();
        loadListeners();
    }

    public void onDisable() {
        ConnectionPool.closeDatabaseConnectionPool();
    }

    private void loadCommands() {
        Objects.requireNonNull(getCommand("ping")).setExecutor(new PingCommand());
        Objects.requireNonNull(getCommand("stats")).setExecutor(new StatsCommand());
        Objects.requireNonNull(getCommand("gm")).setExecutor(new GameModeCommand());
        Objects.requireNonNull(getCommand("pay")).setExecutor(new PayCommand());
        Objects.requireNonNull(getCommand("info")).setExecutor(new InfoCommand());
        Objects.requireNonNull(getCommand("me")).setExecutor(new MeCommand());
        Objects.requireNonNull(getCommand("kiss")).setExecutor(new KissCommand());
        Objects.requireNonNull(getCommand("time")).setExecutor(new TimeCommand());
        Objects.requireNonNull(getCommand("dice")).setExecutor(new DiceCommand());
        Objects.requireNonNull(getCommand("m")).setExecutor(new MessageCommand());
        Objects.requireNonNull(getCommand("show-finances")).setExecutor(new ShowFinancesCommand());
        Objects.requireNonNull(getCommand("deaths")).setExecutor(new DeathsCommand());
        Objects.requireNonNull(getCommand("vanish")).setExecutor(new VanishCommand());
        Objects.requireNonNull(getCommand("slap")).setExecutor(new SlapCommand());
        Objects.requireNonNull(getCommand("house")).setExecutor(new HouseCommand());
        Objects.requireNonNull(getCommand("house")).setTabCompleter(new HouseCommand());
    }

    private void loadListeners() {
        this.pm.registerEvents(new JoinHandler(), this);
        this.pm.registerEvents(new DamageHandler(), this);
        this.pm.registerEvents(new DeathHandler(), this);
        this.pm.registerEvents(new ChatHandler(), this);
        this.pm.registerEvents(new MOTDHandler(), this);
    }

    public static String toJsonString(Object src) {
        return src != null ? gson.toJson(src) : null;
    }

}