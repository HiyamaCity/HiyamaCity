package de.hiyamacity.util;

import de.hiyamacity.main.Main;
import de.hiyamacity.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class PlaytimeTracker {

    public static void startPlaytimeTracker() {

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    Optional<User> user = User.getUser(player.getUniqueId());

                    if (user.map(User::isAfk).orElse(false)) return;
                    long minutes = user.map(User::getPlayedMinutes).orElse(Long.MIN_VALUE);
                    long hours = user.map(User::getPlayedHours).orElse(Long.MIN_VALUE);

                    minutes++;
                    if (minutes >= 60) {
                        minutes = 0;
                        hours++;
                        long finalHours = hours;
                        user.ifPresent(user1 -> user1.setPlayedHours(finalHours));
                    }
                    long finalMinutes = minutes;
                    user.ifPresent(user1 -> {
                        user1.setPlayedMinutes(finalMinutes);
                        user1.update();
                    });
                });
            }
        };

        runnable.runTaskTimer(Main.getInstance(), 20 * 60, 20 * 60);

    }

}