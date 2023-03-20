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
	private static final String OOC_HANDLE = "-";

	@EventHandler
	@SuppressWarnings("deprecation")
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		final Player p = e.getPlayer();
		final String message = e.getMessage();

		if (message.startsWith(ACTION_HANDLE) && message.endsWith(ACTION_HANDLE))
			handleAction(p, message.substring(1, message.length() - 1).trim());
		else if (message.startsWith(WHISPER_HANDLE)) handleWhisper(p, message.replace(WHISPER_HANDLE, "").trim());
		else if (message.startsWith(SHOUT_HANDLE)) handleShout(p, message.replace(SHOUT_HANDLE, "").trim());
		else if (message.startsWith(OOC_HANDLE)) handleOutOfCharacter(p, message.replace(OOC_HANDLE, "").trim());
		else handleRoleplayChat(p, message);

	}

	private static void handleWhisper(Player p, String message) {
		final List<Player> recipients = getRecipients(p, Distance.CHAT_MESSAGE_LARGE.getValue() / 4);
		final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

		for (Player t : recipients) {
			final double distance = p.getLocation().distanceSquared(t.getLocation());
			final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());

			final String verb = (!message.contains("?")) ? trs.getString(CHAT_SAY) : trs.getString(CHAT_ASK);
			message = MessageFormat.format(trs.getString(CHAT_RP), "§8Ⓦ§r ", p.getName(), verb, message);

			if (distance <= Distance.CHAT_MESSAGE_SMALL.getValue() / 4) {
				t.sendMessage(ChatColor.GRAY + message);
				continue;
			}

			if (distance <= Distance.CHAT_MESSAGE_MEDIUM.getValue() / 8) {
				t.sendMessage(ChatColor.DARK_GRAY + message);
			}
		}

		if (recipients.size() == 1 && recipients.contains(p)) {
			String errorMsg = rs.getString("chat.whisper.error.no_recipients");
			errorMsg = MessageFormat.format(errorMsg, WHISPER_HANDLE);
			p.sendMessage(errorMsg);
		}
	}

	private static void handleRoleplayChat(Player p, String message) {
		final List<Player> recipients = getRecipients(p, Distance.CHAT_MESSAGE_LARGE.getValue());
		final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

		for (Player t : recipients) {
			final double distance = p.getLocation().distanceSquared(t.getLocation());
			final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());

			final String verb = (!message.contains("?")) ? trs.getString(CHAT_SAY) : trs.getString(CHAT_ASK);
			message = MessageFormat.format(trs.getString(CHAT_RP), "§3Ⓡ§r ", p.getName(), verb, message);

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

		if (recipients.size() == 1 && recipients.contains(p)) {
			String errorMsg = rs.getString("chat.rp.error.no_recipients");
			errorMsg = MessageFormat.format(errorMsg, SHOUT_HANDLE);
			p.sendMessage(errorMsg);
		}
	}

	private static void handleShout(Player p, String message) {
		final List<Player> recipients = getRecipients(p, Distance.CHAT_MESSAGE_LARGE.getValue() * 2);
		final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
		final StringBuilder stringBuilder = new StringBuilder(message);

		for (Player t : recipients) {
			final double distance = p.getLocation().distanceSquared(t.getLocation());
			final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());

			if (!message.endsWith("!")) stringBuilder.append("!");

			final String verb = (!message.contains("?")) ? trs.getString(CHAT_SAY) : trs.getString(CHAT_ASK);
			message = MessageFormat.format(trs.getString(CHAT_RP), "§cⓈ§r ", p.getName(), verb, stringBuilder.toString());

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

		if (recipients.size() == 1 && recipients.contains(p)) {
			p.sendMessage(rs.getString("chat.shout.error.no_recipients"));
		}
	}

	public static void handleAction(Player p, String message) {
		final List<Player> recipients = getRecipients(p, Distance.CHAT_MESSAGE_LARGE.getValue() / 2);
		final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

		for (Player t : recipients) {
			final double distance = p.getLocation().distanceSquared(t.getLocation());
			final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());

			message = MessageFormat.format(trs.getString("chat.action"), "§dAktion§r ", p.getName(), message);

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

		if (recipients.size() == 1 && recipients.contains(p)) {
			p.sendMessage(rs.getString("chat.action.error.no_recipients"));
		}
	}

	public static void handleOutOfCharacter(Player p, String message) {
		final List<Player> recipients = getRecipients(p, Distance.CHAT_MESSAGE_LARGE.getValue() / 2);
		final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

		for (Player t : recipients) {
			final double distance = p.getLocation().distanceSquared(t.getLocation());
			final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());

			message = MessageFormat.format(trs.getString("chat.action"), "§cOut of Character ", p.getName() + ": ", message);

			if (distance <= Distance.CHAT_MESSAGE_SMALL.getValue() / 2) {
				t.sendMessage(message);
				continue;
			}

			if (distance <= Distance.CHAT_MESSAGE_MEDIUM.getValue() / 2) {
				t.sendMessage(message);
				continue;
			}

			if (distance <= Distance.CHAT_MESSAGE_LARGE.getValue() / 2) {
				t.sendMessage(message);
			}
		}

		if (recipients.size() == 1 && recipients.contains(p)) {
			p.sendMessage(rs.getString("chat.ooc.error.no_recipients"));
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
