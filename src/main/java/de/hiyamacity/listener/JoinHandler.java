package de.hiyamacity.listener;

import de.hiyamacity.database.MySqlPointer;
import de.hiyamacity.items.CapriSonneElfenTrank;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinHandler implements Listener {

    @EventHandler
    public void onEvent(PlayerJoinEvent e) {
        e.setJoinMessage("");
        System.out.println(MySqlPointer.getUser(e.getPlayer().getUniqueId()));
        e.getPlayer().getInventory().addItem(new CapriSonneElfenTrank().getAsItemStack());
    }
}