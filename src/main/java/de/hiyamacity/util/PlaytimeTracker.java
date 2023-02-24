package de.hiyamacity.util;

import de.hiyamacity.Main;
import de.hiyamacity.dao.PlaytimeDAOImpl;
import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.entity.Playtime;
import de.hiyamacity.entity.User;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class PlaytimeTracker {


	public static void startPlaytimeTracker() {

		BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.getOnlinePlayers().forEach(player -> {
					
					UserDAOImpl userDAO = new UserDAOImpl();
					PlaytimeDAOImpl playtimeDAO = new PlaytimeDAOImpl();
					Optional<User> optionalUser = userDAO.getUserByPlayerUniqueID(player.getUniqueId());
					optionalUser.ifPresent(user -> {

						if(user.isAfk()) return;
						
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

		runnable.runTaskTimer(Main.getInstance(), 20 * 60, 20 * 60);

	}

}
