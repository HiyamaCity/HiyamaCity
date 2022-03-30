package de.hiyamacity.listener;

import de.hiyamacity.items.Enchantments;
import de.hiyamacity.items.Katana;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Objects;

public class DamageHandler implements Listener {

    @EventHandler
    public void onEvent(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        Player p = (Player) e.getDamager();
    }
}
