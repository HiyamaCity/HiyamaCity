package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.util.VanishHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.ResourceBundle;

public class VanishCommand implements CommandExecutor {


	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (!(sender instanceof Player p)) return true;
		ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
		if (!p.hasPermission("vanish")) return true;
		switch (args.length) {
			case 0 -> {
				if (!VanishHandler.isVanish(p)) {
					VanishHandler.vanish(p);
					p.sendMessage(rs.getString("vanishSelfActivate"));
				} else {
					VanishHandler.reveal(p);
					p.sendMessage(rs.getString("vanishSelfDeactivate"));
				}
				return true;
			}
			case 1 -> {
				Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(args[0]));
				if (t.isEmpty()) {
					p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
					return true;
				}
				ResourceBundle trs = LanguageHandler.getResourceBundle(t.map(Player::getUniqueId).orElse(null));
				if (!VanishHandler.isVanish(t.orElse(null))) {
					VanishHandler.vanish(t.orElse(null));
					p.sendMessage(rs.getString("vanishSelfActivateOther").replace("%target%", t.map(Player::getName).orElse(null)));
					t.ifPresent(player -> player.sendMessage(trs.getString("vanishOtherActivate").replace("%player%", p.getName())));
				} else {
					VanishHandler.reveal(t.orElse(null));
					p.sendMessage(rs.getString("vanishSelfDeactivateOther").replace("%target%", t.map(Player::getName).orElse(null)));
					t.ifPresent(player -> player.sendMessage(trs.getString("vanishOtherDeactivate").replace("%player%", p.getName())));
				}
				return true;
			}
			default -> p.sendMessage(rs.getString("vanishUsage"));
		}
		return false;
	}
}
