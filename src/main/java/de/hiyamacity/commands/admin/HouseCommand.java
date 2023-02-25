package de.hiyamacity.commands.admin;

import de.hiyamacity.entity.House;
import de.hiyamacity.entity.User;
import de.hiyamacity.util.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
		
		if(!p.hasPermission("hiyamacity.house")) {
			return true;
		}

		// house [create | delete | modify | info]
		// house create
		// house delete
		// house info
		// house modify [door | owner | renter] [add | remove | clear]
		// house modify [door] [add | remove]
		// house modify [owner | renter] [add | remove] [spieler]
		// house modify [door | owner | renter] [clear]

		return false;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player p)) {
			return null;
		}

		// house
		if (args.length == 0) return List.of(
				"create", "delete", "modify", "info"
		);

		// house modify
		if (args.length == 1 && args[0].contains("modify")) {
			return List.of(
					"door", "owner", "renter"
			);
		}

		// house modify door
		if (args.length == 2 && args[0].contains("modify")) {
			return List.of(
					"add", "remove", "clear"
			);
		}

		// house modify owner add
		if (args.length == 3 && args[0].contains("modify") && !args[1].contains("door")) {
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
		// TODO: Method stub
		return Optional.empty();
	}
}
