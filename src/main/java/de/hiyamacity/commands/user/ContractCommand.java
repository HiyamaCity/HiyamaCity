package de.hiyamacity.commands.user;

import de.hiyamacity.lang.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class ContractCommand implements CommandExecutor {

	public static HashMap<UUID, UUID> requests;

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (!(sender instanceof Player p)) return true;
		ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

		if (args.length < 1) {
			p.sendMessage(rs.getString("contractUsage"));
			return true;
		}

		StringBuilder msg = new StringBuilder();
		for (int i = 1; i < args.length; i++) {
			msg.append(args[i]).append(" ");
		}

		Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(args[0]));

		if (t.isEmpty()) {
			p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
			return true;
		}

		//Contract contract = new Contract(p.getUniqueId(), t.map(Player::getUniqueId).orElse(null), msg.toString().trim());

		return false;
	}
}
