package de.hiyamacity.util;

import de.hiyamacity.dao.HouseDAOImpl;
import de.hiyamacity.entity.House;
import de.hiyamacity.entity.User;
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

import java.util.*;
import java.util.stream.Collectors;

public class DoorHandler implements Listener {

	@EventHandler
	public void onHouseDoorOpen(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		final Optional<Block> clickedBlock = Optional.ofNullable(e.getClickedBlock());
		final Optional<BlockData> blockData = clickedBlock.map(Block::getBlockData);
		blockData.ifPresent(data -> {
			if (!(data instanceof Openable)) return;
			final Location clickedBlockLocation = clickedBlock.get().getLocation();

			final Optional<House> houseOptional = isLockedDoor(clickedBlockLocation);
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
			Set<de.hiyamacity.entity.Location> doorLocs = house.getDoorLocations().stream().sorted((o1, o2) -> {
				final Double distance1 = o1.toBukkitLocation().distanceSquared(location);
				final Double distance2 = o2.toBukkitLocation().distanceSquared(location);
				return distance1.compareTo(distance2);
			}).collect(Collectors.toCollection(LinkedHashSet::new));

			for (de.hiyamacity.entity.Location door : doorLocs) {
				final Location doorLocation = door.toBukkitLocation();
				if (doorLocation.getBlock().equals(location.getBlock())) return Optional.of(house);
			}
		}

		return Optional.empty();
	}

	private boolean isAllowedToOpenDoor(@NotNull Player p, @NotNull House house) {
		final Set<User> inhabitants = new HashSet<>() {{
			addAll(house.getRenters());
			addAll(house.getOwners());
		}};

		for (User inhabitant : inhabitants) {
			if (p.getUniqueId().equals(inhabitant.getPlayerUniqueID())) return true;
		}

		return false;
	}

}
