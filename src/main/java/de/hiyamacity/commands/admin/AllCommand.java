package de.hiyamacity.commands.admin;

import de.hiyamacity.util.player.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class AllCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player p)) {
			ResourceBundle rs = LanguageHandler.getResourceBundle();
			sender.sendMessage(rs.getString("playerCommand"));
			return true;
		}

		if (!p.hasPermission("hiyamacity.chat.all")) return true;

		final ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

		if (args.length < 1) {
			p.sendMessage(rs.getString("allUsage"));
			return true;
		}

		String message = String.join(" ", args);

		for (Player t : Bukkit.getOnlinePlayers()) {
			final ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());
			String send = trs.getString("all");
			send = MessageFormat.format(send, p.getName(), message);
			t.sendMessage(send);
		}

		return false;
	}
}
