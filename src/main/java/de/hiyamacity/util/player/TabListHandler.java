package de.hiyamacity.util.player;

import de.hiyamacity.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class TabListHandler {

	private TabListHandler() {
	}

	public static void updateTab() {
		BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				for (Player all : Bukkit.getOnlinePlayers())
					initTab(all);
			}
		};
		runnable.runTaskLaterAsynchronously(JavaPlugin.getPlugin(Main.class), 1);
	}

	@SuppressWarnings("deprecation")
	private static void initTab(Player p) {
		int playerCount = Bukkit.getOnlinePlayers().size();
		ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
		String footer = rs.getString("tabListFooter");
		MessageFormat messageFormat = new MessageFormat(footer, rs.getLocale());
		footer = messageFormat.format(new Object[]{playerCount, Bukkit.getMaxPlayers()});
		p.setPlayerListHeaderFooter(rs.getString("tabListHeader"), footer);
	}

}
