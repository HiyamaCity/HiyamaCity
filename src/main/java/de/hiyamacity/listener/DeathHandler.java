package de.hiyamacity.listener;

import de.hiyamacity.objects.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Optional;

@SuppressWarnings("deprecation")
public class DeathHandler implements Listener {

	@EventHandler
	public void onEvent(PlayerDeathEvent e) {
		e.setDeathMessage("");
		Optional<User> user = User.getUser(e.getEntity().getUniqueId());
		user.ifPresent(user1 -> user1.setDeaths(user1.getDeaths() + 1));
	}
}
