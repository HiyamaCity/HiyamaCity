package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class SeenCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (!sender.hasPermission("seen")) return true;
		ResourceBundle rs = (sender instanceof Player p) ? LanguageHandler.getResourceBundle(p.getUniqueId()) : LanguageHandler.getResourceBundle();
		if (args.length != 1) {
			sender.sendMessage(rs.getString("seenUsage"));
			return true;
		}
		String name = args[0];
		Optional<Player> playerOptional = Optional.ofNullable(Bukkit.getPlayer(name));

		Locale locale = User.getUser(Bukkit.getPlayerUniqueId(sender.getName())).map(User::getLocale).map(de.hiyamacity.objects.Locale::getJavaUtilLocale).orElse(LanguageHandler.getDefaultLocale());
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
		playerOptional.ifPresent(player -> sender.sendMessage(rs.getString("seenMessage").replace("%target%", player.getName()).replace("%date%", dateFormat.format(System.currentTimeMillis()))));
		if (playerOptional.isPresent()) return true;
		Optional<OfflinePlayer> offlinePlayer = Optional.of(Bukkit.getOfflinePlayer(name));
		offlinePlayer.ifPresent(player -> {
			if (player.getLastSeen() == 0) {
				sender.sendMessage(rs.getString("seenPlayerNeverJoined").replace("%target%", name));
				return;
			}
			sender.sendMessage(rs.getString("seenMessage").replace("%target%", Optional.of(player).map(OfflinePlayer::getName).orElse(name)).replace("%date%", dateFormat.format(player.getLastSeen())));
		});
		return false;
	}
}