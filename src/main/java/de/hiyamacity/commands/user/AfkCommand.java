package de.hiyamacity.commands.user;

import de.hiyamacity.util.AfkHandler;
import de.hiyamacity.util.LanguageHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

public class AfkCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		if (!(commandSender instanceof Player)) {
			final ResourceBundle rs = LanguageHandler.getResourceBundle();
			commandSender.sendMessage(rs.getString("playerCommand"));
		}

		assert commandSender instanceof Player;
		final Player p = (Player) commandSender;

		AfkHandler.toggleAfk(p.getUniqueId());

		return false;
	}

}
