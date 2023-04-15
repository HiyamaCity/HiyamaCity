package de.hiyamacity.util.player;

import de.hiyamacity.Main;
import de.hiyamacity.dao.PlaytimeDAOImpl;
import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.jpa.Playtime;
import de.hiyamacity.jpa.User;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class PlaytimeTracker {

	private static final int DELAY = 1200; // 1 Minute

	private PlaytimeTracker() {
	}


	public static void startPlaytimeTracker() {

		final BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.getOnlinePlayers().forEach(player -> {

					final UserDAOImpl userDAO = new UserDAOImpl();
					final PlaytimeDAOImpl playtimeDAO = new PlaytimeDAOImpl();
					final Optional<User> optionalUser = userDAO.getUserByPlayerUniqueId(player.getUniqueId());
					optionalUser.ifPresent(user -> {

						if (user.isAfk()) return;

						if (user.getPlaytime() == null) {
							Playtime playtime = new Playtime();
							playtime.setMinutes(1);
							playtime.setHours(0);
							playtimeDAO.create(playtime);
							user.setPlaytime(playtime);
							userDAO.update(user);
							return;
						}

						Playtime playtime = user.getPlaytime();
						long minutes = playtime.getMinutes();
						long hours = playtime.getHours();

						minutes++;
						if (minutes >= 60) {
							minutes = 0;
							hours++;
							final long finalHours = hours;
							playtime.setHours(finalHours);
						}
						final long finalMinutes = minutes;
						playtime.setMinutes(finalMinutes);
						user.setPlaytime(playtime);
						playtimeDAO.update(playtime);
						userDAO.update(user);
					});
				});
			}
		};

		runnable.runTaskTimer(JavaPlugin.getPlugin(Main.class), DELAY, DELAY);

	}

}
