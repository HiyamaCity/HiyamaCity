package de.hiyamacity.commands.admin;

import de.hiyamacity.dao.HouseDAOImpl;
import de.hiyamacity.dao.LocationDAOImpl;
import de.hiyamacity.entity.House;
import de.hiyamacity.entity.User;
import de.hiyamacity.util.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.*;

public class HouseCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player p)) {
			ResourceBundle rs = LanguageHandler.getResourceBundle();
			sender.sendMessage(rs.getString("playerCommand"));
			return true;
		}

		final UUID uuid = p.getUniqueId();
		final ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);

		if (!p.hasPermission("hiyamacity.house")) {
			return true;
		}

		if (args.length < 1 || args.length > 3) {
			p.sendMessage(rs.getString("houseUsage"));
			return true;
		}

		switch (args[0].toLowerCase()) {
			case "create" -> {
				if (args.length != 1) {
					p.sendMessage("houseCreateUsage");
					return true;
				}

				final de.hiyamacity.entity.Location houseSignLocation = new de.hiyamacity.entity.Location().fromBukkitLocation(getHouseSignLocation(p));
				final House createdHouse = new House();
				final HouseDAOImpl houseDAO = new HouseDAOImpl();
				final LocationDAOImpl locationDAO = new LocationDAOImpl();

				if (!(houseSignLocation.toBukkitLocation().getBlock().getBlockData() instanceof WallSign)) {
					p.sendMessage(rs.getString("houseLookAtSign"));
					return true;
				}

				final List<de.hiyamacity.entity.Location> houseSignLocationExists = locationDAO.findByBukkitLocation(houseSignLocation.toBukkitLocation());

				if (!houseSignLocationExists.isEmpty()) {
					p.sendMessage(rs.getString("houseSignAlreadyRegistered"));
					return true;
				}

				locationDAO.create(houseSignLocation);
				createdHouse.setSignLocation(houseSignLocation);
				houseDAO.create(createdHouse);

				String message = rs.getString("houseCreateSuccessful");
				message = MessageFormat.format(message, createdHouse.getId(), houseSignLocation.getX(), houseSignLocation.getY(), houseSignLocation.getZ());
				p.sendMessage(message);

			}
			case "delete" -> {
				if (args.length != 1) {
					p.sendMessage("houseDeleteUsage");
					return true;
				}

				final Location houseSignLocation = getHouseSignLocation(p);
				final List<de.hiyamacity.entity.Location> optionalLocation = new LocationDAOImpl().findByBukkitLocation(houseSignLocation);

				if (optionalLocation.isEmpty()) {
					p.sendMessage(rs.getString("houseNotFound"));
					return true;
				}

				optionalLocation.forEach(loc -> {
					final Optional<House> houseOptional = getHouse(loc.toBukkitLocation());
					final HouseDAOImpl houseDAO = new HouseDAOImpl();

					if (houseOptional.isEmpty()) {
						p.sendMessage(rs.getString("houseNotFound"));
						return;
					}

					final House house = houseOptional.get();
					boolean success = houseDAO.delete(House.class, house.getId());

					if (!success) {
						p.sendMessage(rs.getString("houseDeleteUnsuccessful"));
						return;
					}

					String message = rs.getString("houseDeleteSuccessful");
					message = MessageFormat.format(message, house.getId(), houseSignLocation.getX(), houseSignLocation.getY(), houseSignLocation.getZ());
					p.sendMessage(message);
				});
			}
		}

		// house [create | delete | modify | info]
		// house create
		// house delete
		// house info
		// house modify [id] [door | owner | renter] [add | remove | clear]
		// house modify [id] [door] [add | remove]
		// house modify [id] [owner | renter] [add | remove] [spieler]
		// house modify [id] [door | owner | renter] [clear]

		return false;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player p)) {
			return null;
		}

		if (args.length == 1) return List.of("create", "delete", "modify", "info");

		if (args.length == 2 && args[0].contains("modify")) {
			return getAllHouseIDs().stream().map(Object::toString).toList();
		}

		if (args.length == 3 && args[0].contains("modify")) {
			return List.of("door", "owner", "renter");
		}

		if (args.length == 4 && args[0].contains("modify")) {
			return List.of("add", "remove", "clear");
		}

		if (args.length == 5 && args[0].contains("modify") && !args[2].contains("door")) {
			switch (args[2].toLowerCase()) {
				case "add" -> {
					final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
					final Optional<House> houseOptional = getHouse(p.getTargetBlock(null, 5).getLocation());

					if (houseOptional.isEmpty()) {
						return List.of(onlinePlayers.stream().map(Player::getName).toString());
					}

					final House house = houseOptional.get();
					final Set<User> inhabitants = new HashSet<>() {{
						addAll(house.getOwners());
						addAll(house.getRenters());
					}};

					return new ArrayList<>() {{
						for (Player onlinePlayer : onlinePlayers) {
							for (User inhabitant : inhabitants) {
								if (!inhabitant.getPlayerUniqueID().equals(onlinePlayer.getUniqueId()))
									add(onlinePlayer.getName());
							}
						}
					}};
				}

				case "remove" -> {
					final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
					final Optional<House> houseOptional = getHouse(p.getTargetBlock(null, 5).getLocation());

					if (houseOptional.isEmpty()) {
						return List.of(onlinePlayers.stream().map(Player::getName).toString());
					}

					final House house = houseOptional.get();

					final Set<User> inhabitants = new HashSet<>() {{
						addAll(house.getOwners());
						addAll(house.getRenters());
					}};

					return new ArrayList<>() {{
						for (User inhabitant : inhabitants) {
							final Optional<Player> player = Optional.ofNullable(Bukkit.getPlayer(inhabitant.getPlayerUniqueID()));
							player.ifPresent(value -> add(value.getName()));
						}
					}};
				}

				default -> {
					return null;
				}
			}
		}

		return null;
	}

	/**
	 * @param location the location of the house sign
	 *
	 * @return the corresponding house to the location
	 */
	private Optional<House> getHouse(@NotNull Location location) {
		return new HouseDAOImpl().getHouseBySignLocation(location);
	}

	private Location getHouseSignLocation(@NotNull Player p) {
		return p.getTargetBlock(null, 5).getLocation();
	}

	private List<Long> getAllHouseIDs() {
		return new HouseDAOImpl().findAll().stream().map(House::getId).toList();
	}

}
