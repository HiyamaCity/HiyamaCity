package de.hiyamacity.listener;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.util.VanishHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CommandHandler implements Listener {

	private final List<String> ignoredCommands = List.of("whisper", "w", "shout", "s", "ooc");

	@EventHandler
	public void onEvent(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (p.hasPermission("skipVanishCheck")) return;
		for (String command : ignoredCommands) {
			if (e.getMessage().startsWith("/" + command)) return;
		}
		Optional<List<Player>> vanishList = Optional.ofNullable(VanishHandler.getVanishPlayers());
		vanishList.ifPresent(vanishPlayers -> {
			vanishPlayers.forEach(player -> {
				String[] messageTokens = e.getMessage().split(" ");
				for (String token : messageTokens) {
					Optional<Player> optionalPlayer = Optional.ofNullable(Bukkit.getPlayer(token));
					optionalPlayer.ifPresent(mentionedPlayer -> {
						if (!mentionedPlayer.getUniqueId().equals(player.getUniqueId())) return;
						e.setCancelled(true);
						ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
						p.sendMessage(rs.getString("playerNotFound").replace("%target%", token));
					});
				}
			});
		});
	}
	
}