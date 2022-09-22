package de.hiyamacity.commands.user;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.misc.Distances;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OocCommand implements CommandExecutor {


	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (!(sender instanceof Player p)) return true;
		ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
		if (args.length < 1) {
			p.sendMessage(rs.getString("oocChatUsage"));
			return true;
		}
		StringBuilder msgBuilder = new StringBuilder();
		for (String arg : args) {
			msgBuilder.append(arg).append(" ");
		}
		String msg = msgBuilder.toString().trim();
		List<Player> recipients = new ArrayList<>(Bukkit.getOnlinePlayers().stream().filter(player -> (player.getLocation().distanceSquared(p.getLocation()) <= Distances.CHAT_MESSAGE_NEAREST)).toList());
		recipients.forEach(player -> {
			ResourceBundle resourceBundle = LanguageHandler.getResourceBundle(player.getUniqueId());
			player.sendMessage(resourceBundle.getString("oocChat").replace("%player%", p.getName()).replace("%msg%", msg));
		});
		return false;
	}
}
