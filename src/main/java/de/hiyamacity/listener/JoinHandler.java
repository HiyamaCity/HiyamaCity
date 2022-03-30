package de.hiyamacity.listener;

import de.hiyamacity.items.Katana;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinHandler implements Listener {

    @EventHandler
    public void onEvent(PlayerJoinEvent e) {
        e.setJoinMessage("");
        e.getPlayer().getInventory().addItem(new Katana().getAsItemStack());
    }
}