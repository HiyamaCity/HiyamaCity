package de.hiyamacity.commands.user;

import de.hiyamacity.util.Distance;
import de.hiyamacity.util.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class MeCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player p)) {
			ResourceBundle rs = LanguageHandler.getResourceBundle();
			sender.sendMessage(rs.getString("playerCommand"));
			return true;
		}

		final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

		if (args.length < 1) {
			p.sendMessage(rs.getString("meUsage"));
			return true;
		}

		String message = String.join(" ", args);
		if (!(message.endsWith("."))) message = message + ".";

		for (Player t : Bukkit.getOnlinePlayers())
			if (p.getLocation().distanceSquared(t.getLocation()) <= Distance.CHAT_MESSAGE_NEAREST) {
				final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());
				String send = trs.getString("me");
				send = MessageFormat.format(send, p.getName(), message);
				t.sendMessage(send);
			}
		return false;
	}
}
