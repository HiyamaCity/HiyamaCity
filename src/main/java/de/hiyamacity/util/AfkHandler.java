package de.hiyamacity.util;

import de.hiyamacity.dao.UserDao;
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

	private static final Location afkLocation = new Location(Bukkit.getWorld("world"), -59, 126, 366, 180, 0);

	private static @NotNull final Location fallBackLocation = new Location(Bukkit.getWorld("world"), -41, 111, 400, -90, 0);

	public static void toggleAfk(@NotNull UUID uuid) {
		Optional<User> userOptional = UserDao.getUserByPlayerUUID(uuid);
		Optional<Player> playerOptional = Optional.ofNullable(Bukkit.getPlayer(uuid));
		userOptional.ifPresent(user -> {
			user.setAfk(!user.isAfk()); // Toggle AFK
			ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);
			
			// user.isAfk is true when the player wasn't afk.
			if(user.isAfk()) {
				AfkLocation nonAfkLocation = new AfkLocation().fromBukkitLocation(playerOptional.map(Entity::getLocation).orElse(fallBackLocation));
				playerOptional.ifPresent(p -> p.sendMessage(nonAfkLocation.toString()));
				user.setNonAfkLocation(nonAfkLocation);
				playerOptional.ifPresent(p -> {
					p.teleport(afkLocation);
					p.sendMessage(rs.getString("afkJoin"));
				});
			} else {
				Location nonAfkLocation = (user.getNonAfkLocation() == null) ? fallBackLocation : user.getNonAfkLocation().toBukkitLocation();
				playerOptional.ifPresent(p -> {
					p.teleport(nonAfkLocation);
					p.sendMessage(rs.getString("afkLeave"));
				});
				user.setNonAfkLocation(null);
			}

			UserDao.updateUser(user);
			RankHandler.updateRanks();
			
		});
	}

}
