package de.hiyamacity;

import de.hiyamacity.commands.user.AfkCommand;
import de.hiyamacity.util.JoinHandler;
import de.hiyamacity.util.RankHandler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private static EntityManagerFactory entityManagerFactory;
	final PluginManager pm = Bukkit.getPluginManager();
	@Getter
	private static JavaPlugin instance;

	@Override
	public void onEnable() {
		instance = this;

		Thread.currentThread().setContextClassLoader(Main.class.getClassLoader());
		entityManagerFactory = Persistence.createEntityManagerFactory("default");

		RankHandler.initScoreboard();

		loadListeners();
		loadCommands();
	}

	@Override
	public void onDisable() {
		entityManagerFactory.close();
	}

	public void loadCommands() {
		getCommand("afk").setExecutor(new AfkCommand());
	}

	public void loadListeners() {
		this.pm.registerEvents(new JoinHandler(), this);
	}
	
	public static EntityManager getEntityManager() {
		return entityManagerFactory.createEntityManager();
	}

}
