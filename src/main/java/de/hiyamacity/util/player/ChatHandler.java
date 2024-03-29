package de.hiyamacity.util.player;

import de.hiyamacity.util.Distance;
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
	private static final String OUT_OF_CHARACTER = "chat.ooc";

	private static void sendWhisperChatMessage(Player t, double distance, String finalMessage) {
		if (distance <= (Distance.CHAT_MESSAGE_SMALL.getValue() / 16)) {
			t.sendMessage("§8Ⓦ " + ChatColor.GRAY + finalMessage);
			return;
		}

		if (distance <= (Distance.CHAT_MESSAGE_MEDIUM.getValue() / 32)) {
			t.sendMessage("§8Ⓦ " + ChatColor.DARK_GRAY + finalMessage);
		}
	}

	private static void sendRoleplayChatMessage(Player t, double distance, String finalMessage) {
		if (distance <= Distance.CHAT_MESSAGE_SMALL.getValue()) {
			t.sendMessage("§3Ⓡ " + ChatColor.WHITE + finalMessage);
			return;
		}

		if (distance <= Distance.CHAT_MESSAGE_MEDIUM.getValue()) {
			t.sendMessage("§3Ⓡ " + ChatColor.GRAY + finalMessage);
			return;
		}

		if (distance <= Distance.CHAT_MESSAGE_LARGE.getValue()) {
			t.sendMessage("§3Ⓡ " + ChatColor.DARK_GRAY + finalMessage);
		}
	}

	private static void sendShoutChatMessage(Player t, double distance, String finalMessage) {
		if (distance <= (Distance.CHAT_MESSAGE_SMALL.getValue() * 2)) {
			t.sendMessage("§cⓈ " + ChatColor.WHITE + finalMessage);
			return;
		}

		if (distance <= (Distance.CHAT_MESSAGE_MEDIUM.getValue() * 2)) {
			t.sendMessage("§cⓈ " + ChatColor.GRAY + finalMessage);
			return;
		}

		if (distance <= (Distance.CHAT_MESSAGE_LARGE.getValue() * 2)) {
			t.sendMessage("§cⓈ " + ChatColor.DARK_GRAY + finalMessage);
		}
	}

	@EventHandler
	@SuppressWarnings("deprecation")
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		final Player p = e.getPlayer();
		final String message = e.getMessage();

		if (message.startsWith(ACTION_HANDLE) && message.endsWith(ACTION_HANDLE)) handleAction(p, message.trim());
		else if (message.startsWith(WHISPER_HANDLE)) handleWhisper(p, message.trim());
		else if (message.startsWith(SHOUT_HANDLE)) handleShout(p, message.trim());
		else if (message.startsWith(OOC_HANDLE)) handleOutOfCharacter(p, message.trim());
		else handleRoleplayChat(p, message);

	}

	private void handleWhisper(Player p, String message) {
		message = message.substring(1).trim();
		final List<Player> recipients = getRecipients(p, Distance.CHAT_MESSAGE_MEDIUM.getValue() / 32);

		for (Player t : recipients) {
			final double distance = p.getLocation().distanceSquared(t.getLocation());
			final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());

			final String verb = (!message.contains("?")) ? trs.getString(CHAT_SAY) : trs.getString(CHAT_ASK);
			final String finalMessage = MessageFormat.format(trs.getString(CHAT_RP), p.getName(), verb, message);

			sendWhisperChatMessage(t, distance, finalMessage);
		}

		if (recipients.size() == 1 && recipients.contains(p)) {
			final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
			String errorMsg = rs.getString("chat.whisper.error.no_recipients");
			errorMsg = MessageFormat.format(errorMsg, WHISPER_HANDLE);
			p.sendMessage(errorMsg);
		}
	}

	private void handleRoleplayChat(Player p, String message) {
		final List<Player> recipients = getRecipients(p, Distance.CHAT_MESSAGE_LARGE.getValue());

		for (Player t : recipients) {
			final double distance = p.getLocation().distanceSquared(t.getLocation());
			final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());

			final String verb = (!message.contains("?")) ? trs.getString(CHAT_SAY) : trs.getString(CHAT_ASK);
			final String finalMessage = MessageFormat.format(trs.getString(CHAT_RP), p.getName(), verb, message);

			sendRoleplayChatMessage(t, distance, finalMessage);
		}

		if (recipients.size() == 1 && recipients.contains(p)) {
			final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
			String errorMsg = rs.getString("chat.rp.error.no_recipients");
			errorMsg = MessageFormat.format(errorMsg, SHOUT_HANDLE);
			p.sendMessage(errorMsg);
		}
	}

	private void handleShout(Player p, String message) {
		message = message.substring(1).trim();
		final List<Player> recipients = getRecipients(p, Distance.CHAT_MESSAGE_LARGE.getValue() * 2);
		final StringBuilder stringBuilder = new StringBuilder(message);
		if (!message.endsWith("!")) stringBuilder.append("!");

		for (Player t : recipients) {
			final double distance = p.getLocation().distanceSquared(t.getLocation());
			final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());

			final String verb = (!message.contains("?")) ? trs.getString("chat.shout") : trs.getString(CHAT_ASK);
			final String finalMessage = MessageFormat.format(trs.getString(CHAT_RP), p.getName(), verb, stringBuilder.toString());

			sendShoutChatMessage(t, distance, finalMessage);
		}

		if (recipients.size() == 1 && recipients.contains(p)) {
			final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
			p.sendMessage(rs.getString("chat.shout.error.no_recipients"));
		}
	}

	public void handleAction(Player p, String message) {
		message = message.substring(1, message.length() - 1);
		final List<Player> recipients = getRecipients(p, Distance.CHAT_MESSAGE_LARGE.getValue() / 2);

		for (Player t : recipients) {
			final double distance = p.getLocation().distanceSquared(t.getLocation());
			final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());

			final String finalMessage = MessageFormat.format(trs.getString("chat.action"), p.getName(), message);

			if (distance <= Distance.CHAT_MESSAGE_MEDIUM.getValue()) {
				t.sendMessage(ChatColor.GRAY + finalMessage);
			}
		}

		if (recipients.size() == 1 && recipients.contains(p)) {
			final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
			p.sendMessage(rs.getString("chat.action.error.no_recipients"));
		}
	}

	public void handleOutOfCharacter(Player p, String message) {
		message = message.substring(1).trim();
		final List<Player> recipients = getRecipients(p, Distance.CHAT_MESSAGE_LARGE.getValue() / 8);

		for (Player t : recipients) {
			final double distance = p.getLocation().distanceSquared(t.getLocation());
			final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());

			final String finalMessage = MessageFormat.format(trs.getString(OUT_OF_CHARACTER), p.getName() + ": ", message);

			if (distance <= (Distance.CHAT_MESSAGE_LARGE.getValue() / 8)) {
				t.sendMessage(finalMessage);
			}
		}

		if (recipients.size() == 1 && recipients.contains(p)) {
			final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
			p.sendMessage(rs.getString("chat.ooc.error.no_recipients"));
		}
	}

	/**
	 * @param p        the given player
	 * @param distance the given distance
	 *
	 * @return list of players in the given distance
	 */
	private List<Player> getRecipients(Player p, double distance) {
		return new ArrayList<>(Bukkit.getOnlinePlayers().stream().filter(player -> (player.getLocation().distanceSquared(p.getLocation()) <= distance)).toList());
	}

}
