package de.hiyamacity.listener;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import de.hiyamacity.util.VanishHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@SuppressWarnings("deprecation")
public class MOTDHandler implements Listener {

    @EventHandler
    public void onEvent(PaperServerListPingEvent e) {
        e.setHidePlayers(true);
        e.setNumPlayers(Bukkit.getOnlinePlayers().size() - VanishHandler.getVanishPlayerCount());
        e.setMotd("   §bHiyamaCity §8| §7Fantasy & Reallife Roleplay §8| §6[1.16.*]\n                     §7§k##§r §e§lWARTUNGSMODUS §7§k##§r");
    }
}