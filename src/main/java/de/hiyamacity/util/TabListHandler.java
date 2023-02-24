package de.hiyamacity.util;

import de.hiyamacity.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class TabListHandler {

	public static void updateTab() {
		BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				for (Player all : Bukkit.getOnlinePlayers())
					initTab(all);
			}
		};
		runnable.runTaskLaterAsynchronously(Main.getInstance(), 1);
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
