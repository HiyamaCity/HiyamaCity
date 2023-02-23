package de.hiyamacity;

import de.hiyamacity.commands.user.AfkCommand;
import de.hiyamacity.commands.user.PayCommand;
import de.hiyamacity.dao.GeneralDAO;
import de.hiyamacity.util.ChatHandler;
import de.hiyamacity.util.JoinHandler;
import de.hiyamacity.util.RankHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	final PluginManager pm = Bukkit.getPluginManager();
	@Getter
	private static JavaPlugin instance;

	@Override
	public void onEnable() {
		instance = this;
		RankHandler.initScoreboard();

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
	}

	public void loadListeners() {
		this.pm.registerEvents(new JoinHandler(), this);
		this.pm.registerEvents(new ChatHandler(), this);
	}

}
