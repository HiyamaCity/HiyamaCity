package de.hiyamacity.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatHandler implements Listener {
	private static final String ACTION_HANDLE = "*";
	private static final String WHISPER_HANDLE = "<";
	private static final String SHOUT_HANDLE = "!";
	private static final String CHAT_SAY = "chat.say";
	private static final String CHAT_ASK = "chat.ask";
	private static final String CHAT_RP = "chat.rp";

	@EventHandler
	@SuppressWarnings("deprecation")
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		final Player p = e.getPlayer();
		final String message = e.getMessage();

		if (message.startsWith(ACTION_HANDLE) && message.endsWith(ACTION_HANDLE)) handleAction(p, message);
		else if (message.startsWith(WHISPER_HANDLE)) handleWhisper(p, message);
		else if (message.startsWith(SHOUT_HANDLE)) handleShout(p, message);
		else handleRoleplayChat(p, message);

	}

	private static void handleWhisper(Player p, String message) {
		final List<Player> recipients = getRecipients(p, Distance.CHAT_MESSAGE_LARGE.getValue() / 4);
		final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

		if (recipients.size() == 1 && recipients.contains(p)) {
			p.sendMessage(rs.getString("chat.shout.error.no_recipients"));
			return;
		}

		for (Player t : recipients) {
			final double distance = p.getLocation().distanceSquared(t.getLocation());
			final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());

			final String verb = (!message.contains("?")) ? trs.getString(CHAT_SAY) : trs.getString(CHAT_ASK);
			message = MessageFormat.format(trs.getString(CHAT_RP), "§8Ⓦ ", p.getName(), verb, message);

			if (distance <= Distance.CHAT_MESSAGE_SMALL.getValue() / 4) {
				t.sendMessage(ChatColor.WHITE + message);
				continue;
			}

			if (distance <= Distance.CHAT_MESSAGE_MEDIUM.getValue() / 8) {
				t.sendMessage(ChatColor.GRAY + message);
			}
		}
	}

	private static void handleRoleplayChat(Player p, String message) {
		final List<Player> recipients = getRecipients(p, Distance.CHAT_MESSAGE_LARGE.getValue());
		final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

		if (recipients.size() == 1 && recipients.contains(p)) {
			String errorMsg = rs.getString("chat.rp.error.no_recipients");
			errorMsg = MessageFormat.format(errorMsg, SHOUT_HANDLE);
			p.sendMessage(errorMsg);
			return;
		}

		for (Player t : recipients) {
			final double distance = p.getLocation().distanceSquared(t.getLocation());
			final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());

			final String verb = (!message.contains("?")) ? trs.getString(CHAT_SAY) : trs.getString(CHAT_ASK);
			message = MessageFormat.format(trs.getString(CHAT_RP), "§3Ⓡ ", p.getName(), verb, message);

			if (distance <= Distance.CHAT_MESSAGE_SMALL.getValue()) {
				t.sendMessage(ChatColor.WHITE + message);
				continue;
			}

			if (distance <= Distance.CHAT_MESSAGE_MEDIUM.getValue()) {
				t.sendMessage(ChatColor.GRAY + message);
				continue;
			}

			if (distance <= Distance.CHAT_MESSAGE_LARGE.getValue()) {
				t.sendMessage(ChatColor.DARK_GRAY + message);
			}
		}
	}

	private static void handleShout(Player p, String message) {
		final List<Player> recipients = getRecipients(p, Distance.CHAT_MESSAGE_LARGE.getValue() * 2);
		final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

		if (recipients.size() == 1 && recipients.contains(p)) {
			p.sendMessage(rs.getString("chat.shout.error.no_recipients"));
			return;
		}

		for (Player t : recipients) {
			final double distance = p.getLocation().distanceSquared(t.getLocation());
			final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());

			final String verb = (!message.contains("?")) ? trs.getString(CHAT_SAY) : trs.getString(CHAT_ASK);
			message = MessageFormat.format(trs.getString(CHAT_RP), "§cⓈ ", p.getName(), verb, message);

			if (distance <= Distance.CHAT_MESSAGE_SMALL.getValue() * 2) {
				t.sendMessage(ChatColor.WHITE + message);
				continue;
			}

			if (distance <= Distance.CHAT_MESSAGE_MEDIUM.getValue() * 2) {
				t.sendMessage(ChatColor.GRAY + message);
				continue;
			}

			if (distance <= Distance.CHAT_MESSAGE_LARGE.getValue() * 2) {
				t.sendMessage(ChatColor.DARK_GRAY + message);
			}
		}
	}

	public static void handleAction(Player p, String message) {
		final List<Player> recipients = getRecipients(p, Distance.CHAT_MESSAGE_LARGE.getValue() / 2);
		final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

		if (recipients.size() == 1 && recipients.contains(p)) {
			p.sendMessage(rs.getString("chat.action.error.no_recipients"));
			return;
		}

		for (Player t : recipients) {
			final double distance = p.getLocation().distanceSquared(t.getLocation());
			final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());

			message = MessageFormat.format(trs.getString(CHAT_RP), "§dAktion ", p.getName(), message);

			if (distance <= Distance.CHAT_MESSAGE_SMALL.getValue() / 2) {
				t.sendMessage(ChatColor.WHITE + message);
				continue;
			}

			if (distance <= Distance.CHAT_MESSAGE_MEDIUM.getValue() / 2) {
				t.sendMessage(ChatColor.GRAY + message);
				continue;
			}

			if (distance <= Distance.CHAT_MESSAGE_LARGE.getValue() / 2) {
				t.sendMessage(ChatColor.DARK_GRAY + message);
			}
		}
	}

	/**
	 * @param p        the given player
	 * @param distance the given distance
	 *
	 * @return list of players in the given distance
	 */
	private static List<Player> getRecipients(Player p, double distance) {
		return new ArrayList<>(Bukkit.getOnlinePlayers().stream().filter(player -> (player.getLocation().distanceSquared(p.getLocation()) <= distance)).toList());
	}

}
