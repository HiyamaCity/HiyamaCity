package de.hiyamacity;

import de.hiyamacity.commands.admin.ATMCommand;
import de.hiyamacity.commands.user.*;
import de.hiyamacity.dao.GeneralDAO;
import de.hiyamacity.util.ChatHandler;
import de.hiyamacity.util.JoinHandler;
import de.hiyamacity.util.PlaytimeTracker;
import de.hiyamacity.util.RankHandler;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private static final EntityManagerFactory entityManagerFactory;

	static {
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

	@SuppressWarnings("ConstantConditions")
	public void loadCommands() {
		getCommand("afk").setExecutor(new AfkCommand());
		getCommand("pay").setExecutor(new PayCommand());
		getCommand("money").setExecutor(new MoneyCommand());
		getCommand("atm").setExecutor(new ATMCommand());
		getCommand("atm").setTabCompleter(new ATMCommand());
		getCommand("bank").setExecutor(new BankCommand());
		getCommand("bank").setTabCompleter(new BankCommand());
		getCommand("playtime").setExecutor(new PlaytimeCommand());
	}

	public void loadListeners() {
		this.pm.registerEvents(new JoinHandler(), this);
		this.pm.registerEvents(new ChatHandler(), this);
	}

	public static EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

}
