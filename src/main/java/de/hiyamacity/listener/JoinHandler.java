package de.hiyamacity.listener;

import de.hiyamacity.database.MySqlPointer;
import de.hiyamacity.objects.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinHandler implements Listener {

    @EventHandler
    public void onEvent(PlayerJoinEvent e) {
        e.setJoinMessage("");
        MySqlPointer.registerUser(e.getPlayer().getUniqueId(), new User(e.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onEvent(PlayerQuitEvent e) {
        e.setQuitMessage("");
    }
}