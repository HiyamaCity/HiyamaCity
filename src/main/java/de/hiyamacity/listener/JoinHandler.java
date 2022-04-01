package de.hiyamacity.listener;

import de.hiyamacity.database.MySqlPointer;
import de.hiyamacity.objects.User;
import de.hiyamacity.util.RankHandler;
import de.hiyamacity.util.TablistHandler;
import de.hiyamacity.util.VanishHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("deprecation")
public class JoinHandler implements Listener {
    
    @EventHandler
    public void onEvent(PlayerJoinEvent e) {
        e.setJoinMessage("");
        MySqlPointer.registerUser(e.getPlayer().getUniqueId(), new User(e.getPlayer().getUniqueId()));
        VanishHandler.updateVanish(e.getPlayer());
        TablistHandler.updateTab();
        RankHandler.applyPrefixes();
    }

    @EventHandler
    public void onEvent(PlayerQuitEvent e) {
        e.setQuitMessage("");
    }

}