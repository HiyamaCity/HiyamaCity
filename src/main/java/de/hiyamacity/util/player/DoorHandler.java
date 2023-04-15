package de.hiyamacity.util.player;

import de.hiyamacity.dao.HouseDAOImpl;
import de.hiyamacity.jpa.House;
import de.hiyamacity.jpa.User;
import de.hiyamacity.util.Util;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class DoorHandler implements Listener {

	@EventHandler
	public void onHouseDoorOpen(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		final Optional<Block> clickedBlock = Optional.ofNullable(e.getClickedBlock());
		final Optional<BlockData> blockData = clickedBlock.map(Block::getBlockData);
		blockData.ifPresent(data -> {
			if (!(data instanceof Openable)) return;
			final Block block = clickedBlock.get();
			final Optional<Location> openableLocation = Util.getOpenableLocation(block);
			if(openableLocation.isEmpty()) return;
			final Optional<House> houseOptional = isLockedDoor(openableLocation.get());
			if (houseOptional.isEmpty()) return;
			final House house = houseOptional.get();
			if (!(isAllowedToOpenDoor(e.getPlayer(), house))) e.setCancelled(true);
		});
	}

	/**
	 * @param location the location of the clicked block
	 *
	 * @return optional of the house that the clicked door belongs to (if present) else empty optional
	 */
	private Optional<House> isLockedDoor(@NotNull Location location) {
		final HouseDAOImpl houseDAO = new HouseDAOImpl();
		final List<House> houses = houseDAO.findAll();

		houses.sort((o1, o2) -> {
			final Double distance1 = o1.getSignLocation().toBukkitLocation().distanceSquared(location);
			final Double distance2 = o2.getSignLocation().toBukkitLocation().distanceSquared(location);
			return distance1.compareTo(distance2);
		});

		for (House house : houses) {
			List<de.hiyamacity.jpa.Location> houseDoorLocs = house.getDoorLocations().stream().toList();
			
			final Stream<de.hiyamacity.jpa.Location> sortedDoorLocs = houseDoorLocs.stream().sorted((o1, o2) -> {
				final Double distance1 = o1.toBukkitLocation().distanceSquared(location);
				final Double distance2 = o2.toBukkitLocation().distanceSquared(location);
				return distance1.compareTo(distance2);
			});
			
			houseDoorLocs = sortedDoorLocs.toList();

			for (de.hiyamacity.jpa.Location door : houseDoorLocs) {
				final Location doorLocation = door.toBukkitLocation();
				if (doorLocation.getBlock().equals(location.getBlock())) return Optional.of(house);
			}
		}

		return Optional.empty();
	}

	/**
	 * @param p     the player
	 * @param house the house of which the doors are checked
	 *
	 * @return true if the player is allowed to open the door
	 */
	private boolean isAllowedToOpenDoor(@NotNull Player p, @NotNull House house) {
		final Set<User> inhabitants = new HashSet<>();
		inhabitants.addAll(house.getOwners());
		inhabitants.addAll(house.getRenters());

		for (User inhabitant : inhabitants) {
			if (p.getUniqueId().equals(inhabitant.getPlayerUniqueID())) return true;
		}

		return false;
	}

}
