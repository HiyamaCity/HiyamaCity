package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.ResourceBundle;

public class KickCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player p)) return true;
		ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
		if (!p.hasPermission("kick")) return true;
		// /kick <Spieler> [Grund]
		switch (args.length) {
			case 0 -> {
				p.sendMessage(rs.getString("kickUsage"));
				return true;
			}
			case 1 -> {
				Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(args[0]));

				if (t.isEmpty()) {
					p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
					return true;
				}

				ResourceBundle trs = LanguageHandler.getResourceBundle(t.map(Player::getUniqueId).orElse(null));
				p.sendMessage(rs.getString("kickMessageKickedNoReason").replace("%target%", t.map(Player::getName).orElse("")));
				t.ifPresent(player -> player.kick(Component.text(trs.getString("kickMessageNoReason")), PlayerKickEvent.Cause.KICK_COMMAND));

				return true;
			}
			default -> {
				Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(args[0]));

				if (t.isEmpty()) {
					p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
					return true;
				}

				StringBuilder reason = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					reason.append(args[i]).append(" ");
				}

				ResourceBundle trs = LanguageHandler.getResourceBundle(t.map(Player::getUniqueId).orElse(null));
				p.sendMessage(rs.getString("kickMessageKicked").replace("%target%", t.map(Player::getName).orElse("")).replace("%reason%", reason.toString().trim()));
				t.ifPresent(player -> player.kick(Component.text(trs.getString("kickMessage").replace("%reason%", reason.toString().trim()).replace("%player%", p.getName())), PlayerKickEvent.Cause.KICK_COMMAND));

				return true;
			}
		}
	}
}
