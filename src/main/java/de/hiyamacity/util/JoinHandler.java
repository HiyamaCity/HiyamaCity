package de.hiyamacity.util;

import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.entity.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class JoinHandler implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage("");
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();

		UserDAOImpl userDAO = new UserDAOImpl();
		Optional<User> userOptional = userDAO.getUserByPlayerUniqueID(uuid);
		if (userOptional.isEmpty()) {
			User user = new User();
			user.setPlayerUniqueID(uuid);
			userDAO.create(user);
		}

		RankHandler.updateRanks();

		ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);
		p.sendMessage(rs.getString("welcomeMessage"));

	}
}
