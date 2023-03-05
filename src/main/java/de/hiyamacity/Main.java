package de.hiyamacity;

import de.hiyamacity.commands.admin.ATMCommand;
import de.hiyamacity.commands.admin.HouseCommand;
import de.hiyamacity.commands.user.*;
import de.hiyamacity.dao.GeneralDAO;
import de.hiyamacity.util.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	@Getter
	private static final EntityManagerFactory entityManagerFactory;

	static {
		// This is used to set the correct context class loader for Hibernate to work correctly
		Thread.currentThread().setContextClassLoader(Main.class.getClassLoader());
		entityManagerFactory = Persistence.createEntityManagerFactory("default");
	}
	
	final PluginManager pm = Bukkit.getPluginManager();
	@Getter
	private static JavaPlugin instance;

	@Override
	public void onEnable() {
		instance = this;
		RankHandler.initScoreboard();
		PlaytimeTracker.startPlaytimeTracker();

		loadListeners();
		loadCommands();
	}

	@Override
	public void onDisable() {
		GeneralDAO.getEntityManagerFactory().close();
	}

	/**
	 * This method is responsible for registering all command that the plugin provides.
	 */
	@SuppressWarnings("ConstantConditions")
	private void loadCommands() {
		getCommand("afk").setExecutor(new AfkCommand());
		getCommand("pay").setExecutor(new PayCommand());
		getCommand("money").setExecutor(new MoneyCommand());
		getCommand("atm").setExecutor(new ATMCommand());
		getCommand("atm").setTabCompleter(new ATMCommand());
		getCommand("bank").setExecutor(new BankCommand());
		getCommand("bank").setTabCompleter(new BankCommand());
		getCommand("playtime").setExecutor(new PlaytimeCommand());
		getCommand("house").setExecutor(new HouseCommand());
		getCommand("house").setTabCompleter(new HouseCommand());
	}

	/**
	 * This method is responsible for registering all events that the plugin listens to.
	 */
	public void loadListeners() {
		this.pm.registerEvents(new JoinHandler(), this);
		this.pm.registerEvents(new ChatHandler(), this);
		this.pm.registerEvents(new DoorHandler(), this);
	}

}
