package de.hiyamacity.util;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.ResourceBundle;

public class AfkHandler {

	private static final Location afkLocation = new Location(Bukkit.getWorld("world"), -59, 126, 366, 180, 0);
	private static final Location fallBackLocation = new Location(Bukkit.getWorld("world"), -41, 111, 400, -90, 0);

	public static void toggleAfk(User user) {
		if (user == null) return;
		user.setAfk(!user.isAfk());
		Optional<Player> p = Optional.ofNullable(Bukkit.getPlayer(user.getUuid()));
		if (p.isEmpty()) return;
		ResourceBundle rs = LanguageHandler.getResourceBundle(p.map(Player::getUniqueId).orElse(null));
		if (user.isAfk()) {
			user.setNonAfkLocation(new de.hiyamacity.objects.Location(p.map(Player::getLocation).orElse(fallBackLocation)));
			p.ifPresent(player -> {
				player.sendMessage(rs.getString("afkJoin"));
				player.teleport(afkLocation);
			});
		} else {
			p.ifPresent(player -> {
				player.sendMessage(rs.getString("afkQuit"));
				player.teleport((user.getNonAfkLocation() == null) ? fallBackLocation : de.hiyamacity.objects.Location.getAsBukkitLocation(user.getNonAfkLocation()));
			});
			user.setNonAfkLocation(null);
		}
		user.update();
		RankHandler.applyPrefixes();
	}
}
