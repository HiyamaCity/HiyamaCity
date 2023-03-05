package de.hiyamacity.util;

import de.hiyamacity.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class ChatHandler implements Listener {

	@EventHandler
	@SuppressWarnings("deprecation")
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		final Player p = e.getPlayer();
		final String message = e.getMessage();

		List<Player> recipients = new ArrayList<>(e.getRecipients().stream().filter(player -> (player.getLocation().distanceSquared(p.getLocation()) <= Distances.CHAT_MESSAGE_FURTHEST)).toList());
		List<String> recipientNames = new ArrayList<>();
		recipients.forEach(player -> recipientNames.add(player.getName()));

		for (Player nearby : recipients) {
			final double distance = p.getLocation().distanceSquared(nearby.getLocation());

			final ResourceBundle rs = LanguageHandler.getResourceBundle(nearby.getUniqueId());
			String formattedMessage = (!message.contains("?")) ? rs.getString("playerSay") : rs.getString("playerAsk");
			formattedMessage = MessageFormat.format(formattedMessage, p.getName(), message);

			final ResourceBundle log = LanguageHandler.getResourceBundle();
			String logMessage = (!message.contains("?")) ? log.getString("playerSay") : log.getString("playerAsk");
			logMessage = MessageFormat.format(logMessage, p.getName(), message);

			Main.getInstance().getLogger().log(Level.INFO, "[CHAT] \"" + logMessage + "\" " + recipientNames);

			if (distance <= Distances.CHAT_MESSAGE_NEAREST) {
				nearby.sendMessage(ChatColor.WHITE + formattedMessage);
				continue;
			}

			if (distance <= Distances.CHAT_MESSAGE_NORMAL) {
				nearby.sendMessage(ChatColor.GRAY + formattedMessage);
				continue;
			}

			if (distance <= Distances.CHAT_MESSAGE_FURTHEST) {
				nearby.sendMessage(ChatColor.DARK_GRAY + formattedMessage);
			}

		}

	}
}