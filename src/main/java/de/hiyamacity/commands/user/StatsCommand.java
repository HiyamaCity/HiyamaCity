package de.hiyamacity.commands.user;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.User;
import de.hiyamacity.util.DecimalSeparator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class StatsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (!(sender instanceof Player p)) return true;
		ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

		switch (args.length) {
			case 0 -> {
				Optional<User> user = User.getUser(p.getUniqueId());
				if (user.isEmpty()) {
					p.sendMessage(rs.getString("playerNotFound").replace("%target%", p.getName()));
					return true;
				}
				long hours = user.map(User::getPlayedHours).orElse(0L);
				long minutes = user.map(User::getPlayedMinutes).orElse(0L);
				long money = user.map(User::getPurse).orElse(0L);
				long bank = user.map(User::getBank).orElse(0L);

				DecimalFormat decimalFormat = DecimalSeparator.prepareFormat(',', '.', false, (byte) 0);

				String msg = LanguageHandler.getResourceBundle(p.getUniqueId()).getString("statsMessage").replace("%target%", p.getName()).replace("%hours%", decimalFormat.format(hours)).replace("%minutes%", decimalFormat.format(minutes)).replace("%money%", decimalFormat.format(money)).replace("%bank%", decimalFormat.format(bank));
				p.sendMessage(msg);
			}
			case 1 -> {
				if (!p.hasPermission("statsOther")) return true;
				Optional<UUID> uuid = Optional.ofNullable(Bukkit.getPlayerUniqueId(args[0]));
				if (uuid.isEmpty()) {
					p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
					return true;
				}
				Optional<User> user = User.getUser(uuid.orElse(null));
				if (user.isEmpty()) {
					p.sendMessage(rs.getString("errorFetchingUser").replace("%target%", args[0]));
					return true;
				}
				long hours = user.map(User::getPlayedHours).orElse(0L);
				long minutes = user.map(User::getPlayedMinutes).orElse(0L);
				long money = user.map(User::getPurse).orElse(0L);
				long bank = user.map(User::getBank).orElse(0L);

				DecimalFormat decimalFormat = DecimalSeparator.prepareFormat(',', '.', false, (byte) 0);

				Optional<Player> optionalPlayer = Optional.ofNullable(Bukkit.getPlayer(uuid.orElse(null)));
				String msg = rs.getString("statsMessage")
						.replace("%target%", optionalPlayer.map(Player::getName).orElseGet(() -> Optional.ofNullable(Bukkit.getOfflinePlayer(uuid.orElse(null)).getName()).orElse("")))
						.replace("%hours%", decimalFormat.format(hours))
						.replace("%minutes%", decimalFormat.format(minutes))
						.replace("%money%", decimalFormat.format(money))
						.replace("%bank%", decimalFormat.format(bank));
				p.sendMessage(msg);
			}
			default -> {
				return true;
			}
		}
		return false;
	}
}
