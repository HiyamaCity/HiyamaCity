package de.hiyamacity.util;

import de.hiyamacity.dao.AfkLocationDAOImpl;
import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.entity.AfkLocation;
import de.hiyamacity.entity.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class AfkHandler {

	private static @NotNull
	final Location afkLocation = new Location(Bukkit.getWorld("world"), -59, 126, 366, 180, 0);

	private static @NotNull
	final Location fallBackLocation = new Location(Bukkit.getWorld("world"), -41, 111, 400, -90, 0);

	public static void toggleAfk(@NotNull UUID uuid) {
		UserDAOImpl userDAO = new UserDAOImpl();
		Optional<User> userOptional = userDAO.getUserByPlayerUniqueID(uuid);
		Optional<Player> playerOptional = Optional.ofNullable(Bukkit.getPlayer(uuid));
		ResourceBundle rs = LanguageHandler.getResourceBundle(playerOptional.map(Entity::getUniqueId).orElse(null));
		userOptional.ifPresent(user -> {
			boolean afk = !user.isAfk();
			user.setAfk(afk);
			afk = user.isAfk();

			if (afk) {
				AfkLocationDAOImpl afkLocationDAO = new AfkLocationDAOImpl();
				AfkLocation nonAfkLocation = new AfkLocation().fromBukkitLocation(playerOptional.map(Player::getLocation).orElse(fallBackLocation));
				nonAfkLocation = afkLocationDAO.create(nonAfkLocation);
				
				if (nonAfkLocation == new AfkLocation().fromBukkitLocation(fallBackLocation)) {
					playerOptional.ifPresent(p -> p.sendMessage(rs.getString("afkFallBackLocationInfo")));
				}

				user.setNonAfkLocation(nonAfkLocation);
				playerOptional.ifPresent(p -> {
					p.teleport(afkLocation);
					p.sendMessage(rs.getString("afkJoin"));
				});
				
			} else {
				Location nonAfkLocation = user.getNonAfkLocation().toBukkitLocation();

				playerOptional.ifPresent(p -> {
					p.teleport(nonAfkLocation);
					p.sendMessage(rs.getString("afkQuit"));
				});
				user.setNonAfkLocation(null);
			}

			userDAO.update(user);

		});
	}

}
