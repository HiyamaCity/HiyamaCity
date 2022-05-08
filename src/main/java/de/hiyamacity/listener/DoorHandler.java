package de.hiyamacity.listener;

import de.hiyamacity.objects.House;
import org.bukkit.block.data.Openable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class DoorHandler implements Listener {

    @EventHandler
    public void onEvent(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!(Objects.requireNonNull(e.getClickedBlock()).getBlockData() instanceof Openable)) return;
        if (!House.isLockedDoor(e.getClickedBlock().getLocation())) return;
        if (!House.allowedToOpen(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation())) e.setCancelled(true);
    }
}
