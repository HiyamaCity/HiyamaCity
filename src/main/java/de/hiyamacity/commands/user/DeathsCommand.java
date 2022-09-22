package de.hiyamacity.commands.user;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.ResourceBundle;

public class DeathsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (!(sender instanceof Player p)) return true;
		ResourceBundle resourceBundle = LanguageHandler.getResourceBundle(p.getUniqueId());
		// TODO: Überarbeiten
		if (args.length != 1) {
			p.sendMessage(resourceBundle.getString("deathUsage"));
			return true;
		}

		Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(args[0]));
		if (t.isEmpty()) {
			p.sendMessage(resourceBundle.getString("playerNotFound").replace("%target%", args[0]));
			return true;
		}

		Optional<User> user = User.getUser(t.map(Player::getUniqueId).orElse(null));
		long deaths = user.map(User::getDeaths).orElse(Long.MIN_VALUE);

		p.sendMessage(resourceBundle.getString("deathCount").replace("%amount%", "" + deaths).replace("%target%", t.map(Player::getName).orElse(null)));

		return false;
	}

}
