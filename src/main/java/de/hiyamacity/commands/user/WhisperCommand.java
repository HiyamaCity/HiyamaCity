package de.hiyamacity.commands.user;

import de.hiyamacity.Main;
import de.hiyamacity.util.Distance;
import de.hiyamacity.util.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class WhisperCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player p)) {
			ResourceBundle rs = LanguageHandler.getResourceBundle();
			sender.sendMessage(rs.getString("playerCommand"));
			return true;
		}

		final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

		if (args.length < 1) {
			p.sendMessage(rs.getString("whisperUsage"));
			return true;
		}

		String message = String.join(" ", args);

		final List<Player> recipients = new ArrayList<>(Bukkit.getOnlinePlayers().stream().filter(player -> (player.getLocation().distanceSquared(p.getLocation()) <= Distance.CHAT_MESSAGE_NEAREST)).toList());
		final List<String> recipientNames = new ArrayList<>();
		recipients.forEach(player -> {
			if (player.getUniqueId().equals(p.getUniqueId())) return;
			recipientNames.add(player.getName());
		});

		final String logMessage = MessageFormat.format("[CHAT] {0}: \"{1}\" {2}", p.getName(), message, recipientNames);
		Main.getInstance().getLogger().log(Level.INFO, logMessage);

		for (Player nearby : recipients) {
			final double distance = p.getLocation().distanceSquared(nearby.getLocation());

			final ResourceBundle trs = LanguageHandler.getResourceBundle(nearby.getUniqueId());
			String formattedMessage = trs.getString("playerWhisper");
			formattedMessage = MessageFormat.format(formattedMessage, p.getName(), message);

			if (distance <= Distance.CHAT_MESSAGE_NEAREST / 2) {
				nearby.sendMessage(ChatColor.GRAY + formattedMessage);
				continue;
			}

			if (distance <= Distance.CHAT_MESSAGE_NEAREST) {
				nearby.sendMessage(ChatColor.DARK_GRAY + formattedMessage);
			}

		}

		return false;
	}
}
