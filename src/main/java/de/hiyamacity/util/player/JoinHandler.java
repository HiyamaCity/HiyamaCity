package de.hiyamacity.util.player;

import de.hiyamacity.Main;
import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.jpa.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class JoinHandler implements Listener {

	@EventHandler
	@SuppressWarnings("deprecation")
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage("");
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();

		UserDAOImpl userDAO = new UserDAOImpl();
		Optional<User> userOptional = userDAO.getUserByPlayerUniqueId(uuid);
		if (userOptional.isEmpty()) {
			User user = createDefaultUser(uuid);
			userDAO.create(user);
		}

		RankHandler.updateRanks();
		TabListHandler.updateTab();
		sendAdminJoinNotification(p);

		ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);
		String message = rs.getString("welcomeMessage");
		message = MessageFormat.format(message, p.getName());
		p.sendMessage(message);

	}

	@EventHandler
	@SuppressWarnings("deprecation")
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage("");
		sendAdminQuitNotification(e.getPlayer());

		final BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				RankHandler.updateRanks();
				TabListHandler.updateTab();
			}
		};
		
		runnable.runTaskLater(JavaPlugin.getPlugin(Main.class), 1);

	}

	private void sendAdminJoinNotification(@NotNull Player p) {
		Bukkit.getOnlinePlayers().forEach(player -> {
			if (player.hasPermission("hiyamacity.join.admin") && !player.getUniqueId().equals(p.getUniqueId())) {
				final ResourceBundle rs = LanguageHandler.getResourceBundle(player.getUniqueId());

				String message = rs.getString("playerJoin");
				message = MessageFormat.format(message, p.getName());
				p.sendMessage(message);
			}
		});
	}

	private void sendAdminQuitNotification(@NotNull Player p) {
		Bukkit.getOnlinePlayers().forEach(player -> {
			if (player.hasPermission("hiyamacity.quit.admin") && !player.getUniqueId().equals(p.getUniqueId())) {
				final ResourceBundle rs = LanguageHandler.getResourceBundle(player.getUniqueId());

				String message = rs.getString("playerQuit");
				message = MessageFormat.format(message, p.getName());
				p.sendMessage(message);
			}
		});
	}

	private @NotNull User createDefaultUser(@NotNull UUID uuid) {
		User user = new User();
		user.setPlayerUniqueID(uuid);
		user.setPurse(4000);
		user.setLocale(Locale.GERMAN);
		return user;
	}

}
