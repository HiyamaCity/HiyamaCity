package de.hiyamacity.commands.user;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.misc.Distances;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class ShoutCommand implements CommandExecutor {


	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player p)) return true;
		ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
		if (args.length == 0) {
			p.sendMessage(rs.getString("shoutUsage"));
			return true;
		}
		String message = String.join(" ", args);
		Collection<? extends Player> recipients = Bukkit.getOnlinePlayers().stream().filter(player -> player.getLocation().distanceSquared(p.getLocation()) <= Distances.CHAT_MESSAGE_FURTHEST * 2).toList();
		ArrayList<String> recipientNames = new ArrayList<>();
		recipients.forEach(player -> recipientNames.add(player.getName()));
		Bukkit.getLogger().log(Level.INFO, "[CHAT] " + p.getName() + " schreit -> " + message + " | " + recipientNames);
		for (Player player : recipients) {
			double distanceSquared = player.getLocation().distanceSquared(p.getLocation());
			ResourceBundle prs = LanguageHandler.getResourceBundle(player.getUniqueId());
			if (distanceSquared <= Distances.CHAT_MESSAGE_NEAREST * 2) {
				player.sendMessage(ChatColor.WHITE + prs.getString("playerShout").replace("%player%", p.getName()) + message);
			} else if (distanceSquared <= Distances.CHAT_MESSAGE_NORMAL * 2) {
				player.sendMessage(ChatColor.GRAY + prs.getString("playerShout").replace("%player%", p.getName()) + message);
			} else if (distanceSquared <= Distances.CHAT_MESSAGE_FURTHEST * 2) {
				player.sendMessage(ChatColor.DARK_GRAY + prs.getString("playerShout").replace("%player%", p.getName()) + message);
			}
		}
		return false;
	}
}
