package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.User;
import de.hiyamacity.util.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.*;

public class CheckBansCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		// returning if the sender is not a player or console command sender.
		if (!(sender instanceof Player || sender instanceof ConsoleCommandSender)) return true;

		// resource bundle of the sender.
		ResourceBundle rs = (sender instanceof Player p) ? LanguageHandler.getResourceBundle(p.getUniqueId()) : LanguageHandler.getResourceBundle();

		// returning if the sender doesn't have this permission.
		if (!sender.hasPermission("checkBans")) return true;

		// returning if the given arguments are the wrong size.
		if (args.length != 1) {
			sender.sendMessage(rs.getString("checkBansUsage"));
			return true;
		}

		// Instantiating a new optional for the given players uuid.
		Optional<UUID> uuid = Optional.ofNullable(Bukkit.getPlayerUniqueId(args[0]));

		// returning if the player is not present.
		if (uuid.isEmpty()) {
			sender.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
			return true;
		}

		// returning if the player has no bans.
		if (!BanManager.hasBans(uuid.orElse(null))) {
			sender.sendMessage(rs.getString("playerWasNotBannedYet").replace("%target%", args[0]));
			return true;
		}

		// Getting the User Object via the player's uuid.
		Optional<User> user = User.getUser(uuid.orElse(null));


		// Get the sender's locale for the correct date formatting.
		Locale locale = (sender instanceof Player p) ?
				User.getUser(p.getUniqueId()).map(User::getLocale)
						.map(de.hiyamacity.objects.Locale::getJavaUtilLocale)
						.orElse(null)
				: LanguageHandler.getDefaultLocale();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);

		// Send the header of the message.
		sender.sendMessage(rs.getString("checkBansHeader")
				.replace("%target%", Optional.ofNullable(
								Bukkit.getPlayer(uuid.orElse(null)))
						.map(Player::getName).orElseGet(() -> Optional.ofNullable(
								Bukkit.getOfflinePlayer(uuid.orElse(null)).getName()).orElse(""))));

		// Get all the Bans of the provided UUID (if present) and loop over every returned ban.
		BanManager.getBans(uuid.orElse(null)).ifPresent(bans ->
				bans.forEach(ban ->
						sender.sendMessage(rs.getString("checkBansMessage")
								.replace("%reason%", (ban.getBanReason() == null) ? "" : ban.getBanReason())
								.replace("%id%", (ban.getBanID() == null) ? "" : ban.getBanID())
								.replace("%banStart%", (ban.getBanStart() == 0) ? "" : dateFormat.format(ban.getBanStart()))
								.replace("%banEnd%", (ban.getBanEnd() == 0) ? "" : dateFormat.format(ban.getBanEnd()))
								.replace("%boolean%", String.valueOf(ban.isActive()))
								.replace("%createdBy%", Optional.ofNullable(
												Bukkit.getPlayer(ban.getCreatedBy()))
										.map(Player::getName).orElseGet(() -> Optional.ofNullable(
												Bukkit.getOfflinePlayer(ban.getCreatedBy()).getName()).orElse(""))))));

		// Send the footer of the message.
		sender.sendMessage(rs.getString("checkBansFooter"));
		return false;
	}
}
