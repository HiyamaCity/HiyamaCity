package de.hiyamacity.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Util {

	public static boolean isLong(String s) {
		try {
			Long.parseLong(s);
			return true;
		} catch (NumberFormatException ignored) {
			return false;
		}
	}
	
	public static Optional<Location> getOpenableLocation(@NotNull Block block) {
		final BlockData blockData = block.getBlockData();
		if(!(blockData instanceof Openable)) return Optional.empty();
		if(blockData instanceof final Door door) {
			final Bisected.Half half = door.getHalf();
			if(half == Bisected.Half.TOP) {
				return Optional.of(block.getLocation().subtract(0, 1, 0));
			} else {
				return Optional.of(block.getLocation());
			}
		}
		return Optional.of(block.getLocation());
	}
}
