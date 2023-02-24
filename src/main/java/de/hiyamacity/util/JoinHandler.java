package de.hiyamacity.util;

import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.entity.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class JoinHandler implements Listener {

	@EventHandler
	@SuppressWarnings("deprecation")
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage("");
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();

		UserDAOImpl userDAO = new UserDAOImpl();
		Optional<User> userOptional = userDAO.getUserByPlayerUniqueId(uuid);
		if (userOptional.isEmpty()) {
			User user = createDefaultUser(uuid);
			userDAO.create(user);
		}

		RankHandler.updateRanks();
		TabListHandler.updateTab();

		ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);
		String message = rs.getString("welcomeMessage");
		message = MessageFormat.format(message, p.getName());
		p.sendMessage(message);

	}

	private @NotNull User createDefaultUser(@NotNull UUID uuid) {
		User user = new User();
		user.setPlayerUniqueID(uuid);
		user.setPurse(4000);
		user.setLocale(Locale.GERMAN);
		return user;
	}

}
