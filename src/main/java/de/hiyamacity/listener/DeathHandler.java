package de.hiyamacity.listener;

import de.hiyamacity.database.MySqlPointer;
import de.hiyamacity.objects.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@SuppressWarnings("deprecation")
public class DeathHandler implements Listener {

    @EventHandler
    public void onEvent(PlayerDeathEvent e) {
        e.setDeathMessage("");
        User user = MySqlPointer.getUser(e.getEntity().getUniqueId());
        user.setDeaths(user.getDeaths() + 1);
        MySqlPointer.updateUser(e.getEntity().getUniqueId(), user);
    }
}
