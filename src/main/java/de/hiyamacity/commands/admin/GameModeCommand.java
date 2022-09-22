package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.ResourceBundle;

public class GameModeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

		if (!(sender instanceof Player p)) return true;


		ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());


		if (!p.hasPermission("system.gm")) return true;
		if (args.length == 0) {
			p.sendMessage(rs.getString("gmUsage"));
			return true;
		}

		if (!isInt(args[0])) {
			p.sendMessage(rs.getString("NaN"));
			return true;
		}

		if (Integer.parseInt(args[0]) > 3 || args[0].startsWith("-")) {
			p.sendMessage(rs.getString("gmInvalidGameMode"));
			return true;
		}

		Optional<GameMode> gm = switch (Integer.parseInt(args[0])) {
			case 0 -> Optional.of(GameMode.SURVIVAL);
			case 1 -> Optional.of(GameMode.CREATIVE);
			case 2 -> Optional.of(GameMode.ADVENTURE);
			case 3 -> Optional.of(GameMode.SPECTATOR);
			default -> Optional.empty();
		};
		if (gm.isEmpty()) return true;
		switch (args.length) {
			case 1 -> {
				p.sendMessage(rs.getString("gmPrefix") + " " + rs.getString("gmSelfChanged").replace("%gameMode%", gm.map(GameMode::name).orElse("").toLowerCase().substring(0, 1).toUpperCase() + gm.map(GameMode::name).orElse("").toLowerCase().substring(1)));
				p.setGameMode(gm.orElse(null));
			}
			case 2 -> {
				Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(args[1]));
				if (t.isEmpty()) {
					p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[1]));
					return true;
				}
				p.sendMessage(rs.getString("gmPrefix") + " " + rs.getString("gmSelfChangedOther").replace("%gameMode%", gm.map(GameMode::name).orElse("").toLowerCase().substring(0, 1).toUpperCase() + gm.map(GameMode::name).orElse("").toLowerCase().substring(1)).replace("%target%", t.map(Player::getName).orElse("")));
				ResourceBundle trs = LanguageHandler.getResourceBundle(t.map(Player::getUniqueId).orElse(null));
				t.ifPresent(player -> player.sendMessage(trs.getString("gmPrefix") + " " + trs.getString("gmOtherChangedOther").replace("%gameMode%", gm.map(GameMode::name).orElse("").toLowerCase().substring(0, 1).toUpperCase() + gm.map(GameMode::name).orElse("").toLowerCase().substring(1)).replace("%player%", p.getName())));
				t.ifPresent(player -> player.setGameMode(gm.orElse(null)));
			}
		}

		return false;
	}

	private boolean isInt(String str) {
		try {
			int i = Integer.parseInt(str);
			return true;
		} catch (NumberFormatException ignored) {
		}
		return false;
	}
}
