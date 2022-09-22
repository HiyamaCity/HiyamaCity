package de.hiyamacity.commands.user;

import de.hiyamacity.lang.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.ResourceBundle;

// Übung von Ina
public class ApologyCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (!(sender instanceof Player p)) return true;
		ResourceBundle resourceBundle = LanguageHandler.getResourceBundle(p.getUniqueId());

		if (args.length < 2) {
			p.sendMessage(resourceBundle.getString("apologyUsage"));
			return true;
		}

		Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(args[0]));

		if (t.isEmpty()) {
			p.sendMessage(resourceBundle.getString("playerNotFound").replace("%target%", args[0]));
			return true;
		}

		if (p.getName().equals(t.map(Player::getName).orElse(null))) {
			p.sendMessage(resourceBundle.getString("apologyNotToYourself"));
			return true;
		}

		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 1; i < args.length; i++) {
			stringBuilder.append(args[i]).append(" ");
		}

		String msg = stringBuilder.toString();

		return false;
	}
}
