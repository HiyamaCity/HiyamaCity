package de.hiyamacity.util;

import de.hiyamacity.main.Main;
import de.hiyamacity.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class PlaytimeTracker {

    public static void startPlaytimeTracker() {

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    User user = User.getUser(player.getUniqueId());
                    if (user == null) return;
                    long minutes = user.getPlayedMinutes();
                    long hours = user.getPlayedHours();

                    minutes++;
                    if (minutes >= 60) {
                        minutes = 0;
                        hours++;
                        user.setPlayedHours(hours);
                    }
                    user.setPlayedMinutes(minutes);
                    user.update(player.getUniqueId());
                });
            }
        };

        runnable.runTaskTimer(Main.getInstance(), 20 * 60, 20 * 60);

    }

}