package de.hiyamacity.listener;

import de.hiyamacity.objects.House;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

public class DoorHandler implements Listener {

	@EventHandler
	public void onEvent(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		Optional<Block> clickedBlock = Optional.ofNullable(e.getClickedBlock());
		Optional<BlockData> blockData = clickedBlock.map(Block::getBlockData);
		if (blockData.isPresent() && blockData.orElse(null) instanceof Openable) return;
		if (!House.isLockedDoor(e.getClickedBlock().getLocation())) return;
		if (!House.allowedToOpen(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation())) e.setCancelled(true);
	}
}
