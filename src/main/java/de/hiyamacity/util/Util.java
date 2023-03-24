package de.hiyamacity.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface Util {

	/**
	 * @param s String to check if it's a long
	 *
	 * @return whether the given string is a long
	 */
	static boolean isLong(String s) {
		try {
			Long.parseLong(s);
			return true;
		} catch (NumberFormatException ignored) {
			return false;
		}
	}

	/**
	 * This method returns just the Location of the Block that it was given if it is not a door.
	 * If the Block is an instance of a door we check whether the upper part or lower part was given.
	 * When the upper part of the door was given this method subtracts 1 from the Y coordinate of the given Blocks location.
	 *
	 * @param block The Block that is looked at
	 *
	 * @return The determined location
	 */
	static Optional<Location> getOpenableLocation(@NotNull Block block) {
		final BlockData blockData = block.getBlockData();
		if (!(blockData instanceof Openable)) return Optional.empty();
		if (blockData instanceof final Door door) {
			final Bisected.Half half = door.getHalf();
			if (half == Bisected.Half.TOP) {
				return Optional.of(block.getLocation().subtract(0, 1, 0));
			} else {
				return Optional.of(block.getLocation());
			}
		}
		return Optional.of(block.getLocation());
	}
}
