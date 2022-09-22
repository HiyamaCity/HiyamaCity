package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.util.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class ClearBansCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player || sender instanceof ConsoleCommandSender)) return true;
		ResourceBundle rs = (sender instanceof Player p) ? LanguageHandler.getResourceBundle(p.getUniqueId()) : LanguageHandler.getResourceBundle();
		if (!sender.hasPermission("clearBans")) return true;
		if (args.length != 1) {
			sender.sendMessage(rs.getString("clearBansUsage"));
			return true;
		}
		Optional<UUID> uuid = Optional.ofNullable(Bukkit.getPlayerUniqueId(args[0]));
		if (uuid.isEmpty()) {
			sender.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
			return true;
		}
		if (!BanManager.hasBans(uuid.orElse(null))) {
			sender.sendMessage(rs.getString("playerWasNotBannedYet").replace("%target%", args[0]));
			return true;
		}

		sender.sendMessage(rs.getString("clearBansSuccessful").replace("%target%", Optional.ofNullable(Bukkit.getPlayer(uuid.orElse(null))).map(Player::getName).orElseGet(() -> Optional.ofNullable(Bukkit.getOfflinePlayer(uuid.orElse(null)).getName()).orElse(""))));
		BanManager.clearBans(uuid.orElse(null));

		return false;
	}
}
