package de.hiyamacity.util;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ResourceBundle;

public class AfkHandler {

    private static final Location afkLocation = new Location(Bukkit.getWorld("world"), -59, 126, 366, 180, 0);
    private static final Location fallBackLocation = new Location(Bukkit.getWorld("world"), -41, 111, 400, -90, 0);

    public static void toggleAfk(User user) {
        if (user == null) return;
        user.setAfk(!user.isAfk());
        Player p = Bukkit.getPlayer(user.getUuid());
        if (p == null) return;
        ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
        if (user.isAfk()) {
            user.setNonAfkLocation(new de.hiyamacity.objects.Location(p.getLocation()));
            p.sendMessage(rs.getString("afkJoin"));
            p.teleport(afkLocation);
        } else {
            p.sendMessage(rs.getString("afkQuit"));
            p.teleport((user.getNonAfkLocation() == null) ? fallBackLocation : user.getNonAfkLocation().getAsBukkitLocation());
            user.setNonAfkLocation(null);
        }

        user.update();
    }
}
