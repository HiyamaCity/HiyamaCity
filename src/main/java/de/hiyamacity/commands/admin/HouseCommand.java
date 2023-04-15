package de.hiyamacity.commands.admin;

import de.hiyamacity.dao.HouseDAOImpl;
import de.hiyamacity.dao.LocationDAOImpl;
import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.jpa.House;
import de.hiyamacity.jpa.User;
import de.hiyamacity.util.Util;
import de.hiyamacity.util.player.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
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

import static de.hiyamacity.util.Util.isLong;

public class HouseCommand implements CommandExecutor, TabCompleter {

	private static final String UNKNOWN_ARGUMENT = "unknownArgument";
	private static final String REMOVE_CASE = "remove";
	private static final String ADD_CASE = "add";
	private static final String CLEAR_CASE = "clear";
	private static final String MODIFY_CASE = "modify";
	private static final String HOUSE_NOT_FOUND = "houseNotFound";
	private static final String PLAYER_NOT_FOUND = "playerNotFound";
	private static final String USER_FETCH_FAILED = "userFetchFailed";
	private static final String CREATE_CASE = "create";
	private static final String INFO_CASE = "info";
	private static final String DOOR_CASE = "door";
	private static final String OWNER_CASE = "owner";
	private static final String RENTER_CASE = "renter";
	private static final String DELETE_CASE = "delete";
	private static final String HOUSE_USAGE_MODIFY_PLAYER = "houseUsageModifyPlayer";
	private static final String HOUSE_USAGE_MODIFY = "houseUsageModifyClear";

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player p)) {
			ResourceBundle rs = LanguageHandler.getResourceBundle();
			sender.sendMessage(rs.getString("playerCommand"));
			return true;
		}

		final UUID uuid = p.getUniqueId();
		final ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);

		if (!p.hasPermission("hiyamacity.commands.admin.house")) {
			return true;
		}

		if (args.length < 1 || args.length > 5) {
			p.sendMessage(rs.getString("houseUsage"));
			return true;
		}

		switch (args[0].toLowerCase()) {
			case CREATE_CASE -> handleCreate(p, rs, args);
			case DELETE_CASE -> handleDelete(p, rs, args);
			case INFO_CASE -> handleInfo(p, rs, args);
			case MODIFY_CASE -> handleModify(p, rs, args);
			default -> {
				String message = rs.getString(UNKNOWN_ARGUMENT);
				message = MessageFormat.format(message, args[0].toLowerCase());
				p.sendMessage(message);
			}
		}
		return false;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			return null;
		}

		if (args.length == 1) return List.of(CREATE_CASE, DELETE_CASE, MODIFY_CASE, INFO_CASE);

		if (args.length == 2 && args[0].contains(MODIFY_CASE)) {
			return getAllHouseIDs().stream().map(Object::toString).toList();
		}

		if (args.length == 3 && args[0].contains(MODIFY_CASE)) {
			return List.of(DOOR_CASE, OWNER_CASE, RENTER_CASE);
		}

		if (args.length == 4 && args[0].contains(MODIFY_CASE)) {
			return List.of(ADD_CASE, REMOVE_CASE, CLEAR_CASE);
		}

		if (args.length == 5 && args[0].contains(MODIFY_CASE) && !args[2].contains(DOOR_CASE)) {
			final List<String> playerNames = new ArrayList<>();
			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				playerNames.add(onlinePlayer.getName());
			}
			return playerNames;
		}

		return null;
	}

	/**
	 * This method handles the creation of a new house
	 *
	 * @param p    the player that ran the command
	 * @param rs   the resource bundle of the player
	 * @param args the given arguments from the command
	 */
	@SuppressWarnings("deprecation")
	private void handleCreate(@NotNull Player p, @NotNull ResourceBundle rs, @NotNull String[] args) {
		if (args.length != 1) {
			p.sendMessage("houseCreateUsage");
			return;
		}

		final de.hiyamacity.jpa.Location houseSignLocation = new de.hiyamacity.jpa.Location().fromBukkitLocation(getTargetBlockLocation(p));
		final House createdHouse = new House();
		final HouseDAOImpl houseDAO = new HouseDAOImpl();
		final LocationDAOImpl locationDAO = new LocationDAOImpl();
		final BlockData blockData = houseSignLocation.toBukkitLocation().getBlock().getBlockData();

		if (!(blockData instanceof WallSign)) {
			p.sendMessage(rs.getString("houseLookAtSign"));
			return;
		}

		final List<de.hiyamacity.jpa.Location> houseSignLocationExists = locationDAO.findByBukkitLocation(houseSignLocation.toBukkitLocation());

		if (!houseSignLocationExists.isEmpty()) {
			p.sendMessage(rs.getString("houseSignAlreadyRegistered"));
			return;
		}

		locationDAO.create(houseSignLocation);
		createdHouse.setSignLocation(houseSignLocation);
		houseDAO.create(createdHouse);

		final Sign sign = (Sign) houseSignLocation.toBukkitLocation().getBlock().getState();
		sign.setLine(0, "");
		sign.setLine(1, "== " + createdHouse.getId() + " ==");
		sign.setLine(2, "");
		sign.setLine(3, "");
		sign.update();

		String message = rs.getString("houseCreateSuccessful");
		message = MessageFormat.format(message, createdHouse.getId(), houseSignLocation.getX(), houseSignLocation.getY(), houseSignLocation.getZ());
		p.sendMessage(message);
	}

	/**
	 * This method handles the deletion of an existing house
	 *
	 * @param p    the player that ran the command
	 * @param rs   the resource bundle of the player
	 * @param args the given arguments from the command
	 */
	@SuppressWarnings("deprecation")
	private void handleDelete(@NotNull Player p, @NotNull ResourceBundle rs, @NotNull String[] args) {
		if (args.length != 1) {
			p.sendMessage("houseDeleteUsage");
			return;
		}

		final Location houseSignLocation = getTargetBlockLocation(p);
		final List<de.hiyamacity.jpa.Location> optionalLocation = new LocationDAOImpl().findByBukkitLocation(houseSignLocation);

		if (optionalLocation.isEmpty()) {
			p.sendMessage(rs.getString(HOUSE_NOT_FOUND));
			return;
		}

		optionalLocation.forEach(loc -> {
			final Optional<House> houseOptional = getHouse(loc.toBukkitLocation());
			final HouseDAOImpl houseDAO = new HouseDAOImpl();

			if (houseOptional.isEmpty()) {
				p.sendMessage(rs.getString(HOUSE_NOT_FOUND));
				return;
			}

			final House house = houseOptional.get();
			boolean success = houseDAO.delete(House.class, house.getId());

			if (!success) {
				p.sendMessage(rs.getString("houseDeleteUnsuccessful"));
				return;
			}

			final Sign sign = (Sign) houseSignLocation.getBlock().getState();
			sign.setLine(0, "");
			sign.setLine(1, "");
			sign.setLine(2, "");
			sign.setLine(3, "");
			sign.update();

			String message = rs.getString("houseDeleteSuccessful");
			message = MessageFormat.format(message, house.getId(), houseSignLocation.getX(), houseSignLocation.getY(), houseSignLocation.getZ());
			p.sendMessage(message);
		});
	}

	/**
	 * This method handles printing information about the current house.
	 * The current house is determined by the block the player is looking at
	 * if the block that is looked at is registered to a house all available information will be sent to the player
	 *
	 * @param p    the player that ran the command
	 * @param rs   the resource bundle of the player
	 * @param args the given arguments from the command
	 */
	private void handleInfo(@NotNull Player p, @NotNull ResourceBundle rs, @NotNull String[] args) {
		if (args.length != 1) {
			p.sendMessage("houseDeleteUsage");
			return;
		}

		final Location houseSignLocation = getTargetBlockLocation(p);
		final List<de.hiyamacity.jpa.Location> optionalLocation = new LocationDAOImpl().findByBukkitLocation(houseSignLocation);

		if (optionalLocation.isEmpty()) {
			p.sendMessage(rs.getString(HOUSE_NOT_FOUND));
			return;
		}

		optionalLocation.forEach(location -> {
			final Optional<House> houseOptional = getHouse(location.toBukkitLocation());

			if (houseOptional.isEmpty()) {
				p.sendMessage(rs.getString(HOUSE_NOT_FOUND));
				return;
			}

			final House house = houseOptional.get();
			final List<String> owners = new ArrayList<>();
			house.getOwners().forEach(user -> {
				Optional<Player> player = Optional.ofNullable(Bukkit.getPlayer(user.getPlayerUniqueID()));
				player.ifPresent(value -> owners.add(value.getName()));
			});

			final List<String> renters = new ArrayList<>();
			house.getRenters().forEach(user -> {
				Optional<Player> player = Optional.ofNullable(Bukkit.getPlayer(user.getPlayerUniqueID()));
				player.ifPresent(value -> renters.add(value.getName()));
			});

			final StringBuilder sb = new StringBuilder();
			house.getDoorLocations().forEach(door -> {
				String message = rs.getString("houseInfoDoorLocs");
				message = MessageFormat.format(message, door.getX(), door.getY(), door.getZ());
				sb.append(message);
			});
			final String doorLocations = sb.toString();

			String message = rs.getString("houseInfo");
			message = MessageFormat.format(message, house.getId(), house.getSignLocation().getX(), house.getSignLocation().getY(), house.getSignLocation().getZ(), owners, renters, doorLocations);
			p.sendMessage(message);
		});
	}

	/**
	 * This method is just a helper method. It parses user input and gives it to other methods.
	 *
	 * @param p    the player that ran the command
	 * @param rs   the resource bundle of the player
	 * @param args the given arguments from the command
	 */
	private void handleModify(@NotNull Player p, @NotNull ResourceBundle rs, @NotNull String[] args) {
		if (args.length < 4) {
			p.sendMessage(rs.getString("houseUsageModify"));
			return;
		}

		if (!isLong(args[1])) {
			p.sendMessage(rs.getString("inputNaN"));
			return;
		}

		final long houseNumber = Long.parseLong(args[1]);
		final HouseDAOImpl houseDAO = new HouseDAOImpl();
		final House house = houseDAO.read(House.class, houseNumber);
		switch (args[2].toLowerCase()) {
			case DOOR_CASE -> {
				switch (args[3].toLowerCase()) {
					case ADD_CASE -> houseAddDoor(p, rs, houseDAO, house, args);
					case REMOVE_CASE -> houseDeleteDoor(p, rs, houseDAO, house, args);
					case CLEAR_CASE -> houseClearDoors(p, rs, houseDAO, house, args);
					default -> {
						String message = rs.getString(UNKNOWN_ARGUMENT);
						message = MessageFormat.format(message, args[3].toLowerCase());
						p.sendMessage(message);
					}
				}

			}
			case OWNER_CASE -> {
				switch (args[3].toLowerCase()) {
					case ADD_CASE -> houseAddOwner(p, rs, houseDAO, house, args);
					case REMOVE_CASE -> houseDeleteOwner(p, rs, houseDAO, house, args);
					case CLEAR_CASE -> houseClearOwners(p, rs, houseDAO, house, args);
					default -> {
						String message = rs.getString(UNKNOWN_ARGUMENT);
						message = MessageFormat.format(message, args[3].toLowerCase());
						p.sendMessage(message);
					}
				}
			}
			case RENTER_CASE -> {
				switch (args[3].toLowerCase()) {
					case ADD_CASE -> houseAddRenter(p, rs, houseDAO, house, args);
					case REMOVE_CASE -> houseDeleteRenter(p, rs, houseDAO, house, args);
					case CLEAR_CASE -> houseClearRenters(p, rs, houseDAO, house, args);
					default -> {
						String message = rs.getString(UNKNOWN_ARGUMENT);
						message = MessageFormat.format(message, args[3].toLowerCase());
						p.sendMessage(message);
					}
				}
			}
			default -> {
				String message = rs.getString(UNKNOWN_ARGUMENT);
				message = MessageFormat.format(message, args[2].toLowerCase());
				p.sendMessage(message);
			}
		}
	}

	/**
	 * This method clears all the renters from a given house.
	 *
	 * @param p        the player that ran the command
	 * @param rs       the resource bundle of the player
	 * @param houseDAO the data access object for saving the data to the database
	 * @param house    the determined house
	 * @param args     the given arguments from the command
	 */
	private void houseClearRenters(@NotNull Player p, @NotNull ResourceBundle rs, @NotNull HouseDAOImpl houseDAO, @NotNull House house, @NotNull String[] args) {
		if (args.length != 4) {
			p.sendMessage(rs.getString(HOUSE_USAGE_MODIFY));
			return;
		}

		final Set<User> houseRenters = house.getRenters();
		houseRenters.clear();

		house.setRenters(houseRenters);
		houseDAO.update(house);

		String message = rs.getString("houseSuccessfullyClearedRenters");
		message = MessageFormat.format(message, house.getId());
		p.sendMessage(message);
	}

	/**
	 * This method deletes a specific renter from a given house.
	 *
	 * @param p        the player that ran the command
	 * @param rs       the resource bundle of the player
	 * @param houseDAO the data access object for saving the data to the database
	 * @param house    the determined house
	 * @param args     the given arguments from the command
	 */
	private void houseDeleteRenter(@NotNull Player p, @NotNull ResourceBundle rs, @NotNull HouseDAOImpl houseDAO, @NotNull House house, @NotNull String[] args) {
		if (args.length != 5) {
			p.sendMessage(rs.getString(HOUSE_USAGE_MODIFY_PLAYER));
			return;
		}

		final Optional<UUID> uuidOptional = Optional.ofNullable(Bukkit.getPlayerUniqueId(args[4]));

		if (uuidOptional.isEmpty()) {
			String message = rs.getString(PLAYER_NOT_FOUND);
			message = MessageFormat.format(message, args[4]);
			p.sendMessage(message);
			return;
		}

		final UUID targetUUID = uuidOptional.get();
		final UserDAOImpl userDAO = new UserDAOImpl();
		final Optional<User> optionalUser = userDAO.getUserByPlayerUniqueId(targetUUID);

		if (optionalUser.isEmpty()) {
			p.sendMessage(rs.getString(USER_FETCH_FAILED));
			return;
		}

		final User targetUser = optionalUser.get();
		Set<User> renters = house.getRenters();

		if (!renters.contains(targetUser)) {
			String message = rs.getString("playerNotRenterOfThisHouse");
			message = MessageFormat.format(message, args[4]);
			p.sendMessage(message);
			return;
		}

		renters.remove(targetUser);
		house.setRenters(renters);
		houseDAO.update(house);

		final String targetPlayerName = Optional.ofNullable(Bukkit.getPlayer(targetUUID)).map(Player::getName).orElse(args[4]);
		String message = rs.getString("houseSuccessfullyRemovedRenter");
		message = MessageFormat.format(message, house.getId(), targetPlayerName);
		p.sendMessage(message);
	}

	/**
	 * This method adds a specific renter to a given house.
	 *
	 * @param p        the player that ran the command
	 * @param rs       the resource bundle of the player
	 * @param houseDAO the data access object for saving the data to the database
	 * @param house    the determined house
	 * @param args     the given arguments from the command
	 */
	private void houseAddRenter(@NotNull Player p, @NotNull ResourceBundle rs, @NotNull HouseDAOImpl houseDAO, @NotNull House house, @NotNull String[] args) {
		if (args.length != 5) {
			p.sendMessage(rs.getString(HOUSE_USAGE_MODIFY_PLAYER));
			return;
		}

		final Optional<UUID> uuidOptional = Optional.ofNullable(Bukkit.getPlayerUniqueId(args[4]));

		if (uuidOptional.isEmpty()) {
			String message = rs.getString(PLAYER_NOT_FOUND);
			message = MessageFormat.format(message, args[4]);
			p.sendMessage(message);
			return;
		}

		final UUID targetUUID = uuidOptional.get();
		final UserDAOImpl userDAO = new UserDAOImpl();
		final Optional<User> optionalUser = userDAO.getUserByPlayerUniqueId(targetUUID);

		if (optionalUser.isEmpty()) {
			p.sendMessage(rs.getString(USER_FETCH_FAILED));
			return;
		}

		final User targetUser = optionalUser.get();
		final Set<User> renters = house.getRenters();
		final Set<User> owners = house.getOwners();

		if (renters.contains(targetUser)) {
			String message = rs.getString("playerAlreadyRenterOfThisHouse");
			message = MessageFormat.format(message, args[4]);
			p.sendMessage(message);
			return;
		}

		if (owners.contains(targetUser)) {
			String message = rs.getString("houseModifyPlayerAlreadyOwner");
			message = MessageFormat.format(message, args[4]);
			p.sendMessage(message);
			return;
		}

		renters.add(targetUser);
		house.setRenters(renters);
		houseDAO.update(house);

		final String targetPlayerName = Optional.ofNullable(Bukkit.getPlayer(targetUUID)).map(Player::getName).orElse(args[4]);
		String message = rs.getString("houseSuccessfullyAddedRenter");
		message = MessageFormat.format(message, house.getId(), targetPlayerName);
		p.sendMessage(message);
	}

	/**
	 * This method clears all the owners from a given house.
	 *
	 * @param p        the player that ran the command
	 * @param rs       the resource bundle of the player
	 * @param houseDAO the data access object for saving the data to the database
	 * @param house    the determined house
	 * @param args     the given arguments from the command
	 */
	private void houseClearOwners(@NotNull Player p, @NotNull ResourceBundle rs, @NotNull HouseDAOImpl houseDAO, @NotNull House house, @NotNull String[] args) {
		if (args.length != 4) {
			p.sendMessage(rs.getString(HOUSE_USAGE_MODIFY));
			return;
		}

		final Set<User> houseOwners = house.getOwners();
		houseOwners.clear();

		house.setOwners(houseOwners);
		houseDAO.update(house);

		String message = rs.getString("houseSuccessfullyClearedOwners");
		message = MessageFormat.format(message, house.getId());
		p.sendMessage(message);
	}

	/**
	 * This method deletes a specific owner from a given house.
	 *
	 * @param p        the player that ran the command
	 * @param rs       the resource bundle of the player
	 * @param houseDAO the data access object for saving the data to the database
	 * @param house    the determined house
	 * @param args     the given arguments from the command
	 */
	private void houseDeleteOwner(@NotNull Player p, @NotNull ResourceBundle rs, @NotNull HouseDAOImpl houseDAO, @NotNull House house, @NotNull String[] args) {
		if (args.length != 5) {
			p.sendMessage(rs.getString(HOUSE_USAGE_MODIFY_PLAYER));
			return;
		}

		final Optional<UUID> uuidOptional = Optional.ofNullable(Bukkit.getPlayerUniqueId(args[4]));

		if (uuidOptional.isEmpty()) {
			String message = rs.getString(PLAYER_NOT_FOUND);
			message = MessageFormat.format(message, args[4]);
			p.sendMessage(message);
			return;
		}

		final UUID targetUUID = uuidOptional.get();
		final UserDAOImpl userDAO = new UserDAOImpl();
		final Optional<User> optionalUser = userDAO.getUserByPlayerUniqueId(targetUUID);

		if (optionalUser.isEmpty()) {
			p.sendMessage(rs.getString(USER_FETCH_FAILED));
			return;
		}

		final User targetUser = optionalUser.get();
		Set<User> owners = house.getOwners();

		if (!owners.contains(targetUser)) {
			String message = rs.getString("playerNotOwnerOfThisHouse");
			message = MessageFormat.format(message, args[4]);
			p.sendMessage(message);
			return;
		}

		owners.remove(targetUser);
		house.setOwners(owners);
		houseDAO.update(house);

		final String targetPlayerName = Optional.ofNullable(Bukkit.getPlayer(targetUUID)).map(Player::getName).orElse(args[4]);
		String message = rs.getString("houseSuccessfullyRemovedOwner");
		message = MessageFormat.format(message, house.getId(), targetPlayerName);
		p.sendMessage(message);
	}

	/**
	 * This method adds a specific owner to a given house.
	 *
	 * @param p        the player that ran the command
	 * @param rs       the resource bundle of the player
	 * @param houseDAO the data access object for saving the data to the database
	 * @param house    the determined house
	 * @param args     the given arguments from the command
	 */
	private void houseAddOwner(@NotNull Player p, @NotNull ResourceBundle rs, @NotNull HouseDAOImpl houseDAO, @NotNull House house, @NotNull String[] args) {
		if (args.length != 5) {
			p.sendMessage(rs.getString(HOUSE_USAGE_MODIFY_PLAYER));
			return;
		}

		final Optional<UUID> uuidOptional = Optional.ofNullable(Bukkit.getPlayerUniqueId(args[4]));

		if (uuidOptional.isEmpty()) {
			String message = rs.getString(PLAYER_NOT_FOUND);
			message = MessageFormat.format(message, args[4]);
			p.sendMessage(message);
			return;
		}

		final UUID targetUUID = uuidOptional.get();
		final UserDAOImpl userDAO = new UserDAOImpl();
		final Optional<User> optionalUser = userDAO.getUserByPlayerUniqueId(targetUUID);

		if (optionalUser.isEmpty()) {
			p.sendMessage(rs.getString(USER_FETCH_FAILED));
			return;
		}

		final User targetUser = optionalUser.get();
		final Set<User> owners = house.getOwners();
		final Set<User> renters = house.getRenters();

		if (owners.contains(targetUser)) {
			String message = rs.getString("playerAlreadyOwnerOfThisHouse");
			message = MessageFormat.format(message, args[4]);
			p.sendMessage(message);
			return;
		}

		if (renters.contains(targetUser)) {
			String message = rs.getString("houseModifyPlayerAlreadyRenter");
			message = MessageFormat.format(message, args[4]);
			p.sendMessage(message);
			return;
		}

		owners.add(targetUser);
		house.setOwners(owners);
		houseDAO.update(house);

		final String targetPlayerName = Optional.ofNullable(Bukkit.getPlayer(targetUUID)).map(Player::getName).orElse(args[4]);
		String message = rs.getString("houseSuccessfullyAddedOwner");
		message = MessageFormat.format(message, house.getId(), targetPlayerName);
		p.sendMessage(message);
	}

	/**
	 * This method clears all doors from a given house.
	 *
	 * @param p        the player that ran the command
	 * @param rs       the resource bundle of the player
	 * @param houseDAO the data access object for saving the data to the database
	 * @param house    the determined house
	 * @param args     the given arguments from the command
	 */
	private void houseClearDoors(@NotNull Player p, @NotNull ResourceBundle rs, @NotNull HouseDAOImpl houseDAO, @NotNull House house, @NotNull String[] args) {
		if (args.length != 4) {
			p.sendMessage(rs.getString(HOUSE_USAGE_MODIFY));
			return;
		}

		final Set<de.hiyamacity.jpa.Location> doorLocs = house.getDoorLocations();
		doorLocs.clear();

		house.setDoorLocations(doorLocs);
		houseDAO.update(house);

		String message = rs.getString("houseModifyDoorClearSuccessful");
		message = MessageFormat.format(message, house.getId());
		p.sendMessage(message);
	}

	/**
	 * This method deletes a specific door from a given house.
	 *
	 * @param p        the player that ran the command
	 * @param rs       the resource bundle of the player
	 * @param houseDAO the data access object for saving the data to the database
	 * @param house    the determined house
	 * @param args     the given arguments from the command
	 */
	private void houseDeleteDoor(@NotNull Player p, @NotNull ResourceBundle rs, @NotNull HouseDAOImpl houseDAO, @NotNull House house, @NotNull String[] args) {
		if (args.length != 4) {
			p.sendMessage(rs.getString("houseUsageModifyDoor"));
			return;
		}

		final Location targetLocation = getTargetBlockLocation(p);
		final Optional<Location> openableLocation = Util.getOpenableLocation(targetLocation.getBlock());

		if (openableLocation.isEmpty()) {
			p.sendMessage(rs.getString("houseNonOpenable"));
			return;
		}

		final Location doorLocation = openableLocation.get();
		final Set<de.hiyamacity.jpa.Location> doorLocs = house.getDoorLocations();

		boolean success = doorLocs.removeIf(location -> Objects.equals(location.getWorld(), doorLocation.getWorld().getName()) && location.getX() == doorLocation.getX() && location.getY() == doorLocation.getY() && location.getZ() == doorLocation.getZ());

		if (!success) {
			String message = rs.getString("houseModifyDeleteDoorUnsuccessful");
			message = MessageFormat.format(message, house.getId(), doorLocation.getX(), doorLocation.getY(), doorLocation.getZ());
			p.sendMessage(message);
			return;
		}

		house.setDoorLocations(doorLocs);
		houseDAO.update(house);

		String message = rs.getString("houseModifyDoorDeleteSuccessful");
		message = MessageFormat.format(message, house.getId(), doorLocation.getX(), doorLocation.getY(), doorLocation.getZ());
		p.sendMessage(message);
	}

	/**
	 * This method adds a specific door to a given house.
	 *
	 * @param p        the player that ran the command
	 * @param rs       the resource bundle of the player
	 * @param houseDAO the data access object for saving the data to the database
	 * @param house    the determined house
	 * @param args     the given arguments from the command
	 */
	private void houseAddDoor(@NotNull Player p, @NotNull ResourceBundle rs, @NotNull HouseDAOImpl houseDAO, @NotNull House house, @NotNull String[] args) {
		if (args.length != 4) {
			p.sendMessage(rs.getString("houseUsageModifyDoor"));
			return;
		}

		final Location targetLocation = getTargetBlockLocation(p);
		final Optional<Location> openableLocation = Util.getOpenableLocation(targetLocation.getBlock());

		if (openableLocation.isEmpty()) {
			p.sendMessage(rs.getString("houseNonOpenable"));
			return;
		}

		final Location doorLocation = openableLocation.get();
		final Set<de.hiyamacity.jpa.Location> doorLocs = house.getDoorLocations();
		doorLocs.add(new de.hiyamacity.jpa.Location().fromBukkitLocation(doorLocation));

		house.setDoorLocations(doorLocs);
		houseDAO.update(house);

		String message = rs.getString("houseModifyDoorAddSuccessful");
		message = MessageFormat.format(message, house.getId(), doorLocation.getX(), doorLocation.getY(), doorLocation.getZ());
		p.sendMessage(message);
	}

	/**
	 * This method is responsible for returning a House if there is one that exists at a given location.
	 *
	 * @param location the location of the house sign
	 *
	 * @return the corresponding house to the location
	 */
	private Optional<House> getHouse(@NotNull Location location) {
		return new HouseDAOImpl().getHouseBySignLocation(location);
	}

	/**
	 * This method returns the location of the block that is targeted by the player with a maximum distance of 5
	 *
	 * @param p The player
	 *
	 * @return location of the targeted block
	 */
	private Location getTargetBlockLocation(@NotNull Player p) {
		return p.getTargetBlock(null, 5).getLocation();
	}

	/**
	 * This method finds all the houses that exist and return them as a list
	 *
	 * @return all registered houses
	 */
	private List<Long> getAllHouseIDs() {
		return new HouseDAOImpl().findAll().stream().map(House::getId).toList();
	}

}
