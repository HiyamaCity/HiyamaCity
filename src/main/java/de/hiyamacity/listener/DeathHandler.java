package de.hiyamacity.listener;

import de.hiyamacity.objects.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@SuppressWarnings("deprecation")
public class DeathHandler implements Listener {

    @EventHandler
    public void onEvent(PlayerDeathEvent e) {
        e.setDeathMessage("");
        User user = User.getUser(e.getEntity().getUniqueId());
        if(user == null) return;
        user.setDeaths(user.getDeaths() + 1);
        User.updateUser(e.getEntity().getUniqueId(), user);
    }
}
