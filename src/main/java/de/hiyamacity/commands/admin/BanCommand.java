package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.Ban;
import de.hiyamacity.objects.User;
import de.hiyamacity.util.BanManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class BanCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		// returning if the sender is not a player or console command sender.
		if (!(sender instanceof Player || sender instanceof ConsoleCommandSender)) return true;

		// resource bundle of the sender.
		ResourceBundle rs = (sender instanceof Player p) ? LanguageHandler.getResourceBundle(p.getUniqueId()) : LanguageHandler.getResourceBundle();

		// returning if the sender doesn't have this permission.
		if (!sender.hasPermission("ban")) return true;

		// switching for the argument length.
		switch (args.length) {

			// Sending usage to the player.
			case 0 -> {
				sender.sendMessage(rs.getString("banUsage"));
				return true;
			}

			// Ban without reason.
			case 1 -> {

				// Optional of the players uuid.
				Optional<UUID> uuid = Optional.ofNullable(Bukkit.getPlayerUniqueId(args[0]));

				// returning if the player doesn't exist.
				if (uuid.isEmpty()) {
					sender.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
					return true;
				}

				// Ban with and without punisher's uuid
				if (sender instanceof Player p) BanManager.ban(uuid.orElse(null), p.getUniqueId());
				else BanManager.ban(uuid.orElse(null));

				// Sending the confirmation of the ban to the sender. 
				sender.sendMessage(rs.getString("banMessageNoReasonSelf")
						.replace("%target%", Optional.ofNullable(
										Bukkit.getPlayer(uuid.orElse(null))).map(Player::getName)
								.orElseGet(() -> Optional.ofNullable(
										Bukkit.getOfflinePlayer(uuid.orElse(null)).getName()).orElse(""))));

				// Getting the latest ban.
				Optional<Ban> ban = BanManager.getLatestBan(uuid.orElse(null));

				// Getting the player of the latest ban.
				Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(uuid.orElse(null)));

				// Returning if the player isn't present.
				if (t.isEmpty()) return true;

				// Initiating the player's resource bundle.
				ResourceBundle trs = LanguageHandler.getResourceBundle(uuid.orElse(null));

				// Initiating the player's user object.
				Optional<User> user = User.getUser(uuid.orElse(null));

				// Getting the locale of the user for correct formatting.
				Locale locale = user.map(User::getLocale)
						.map(de.hiyamacity.objects.Locale::getJavaUtilLocale).
						orElse(LanguageHandler.getDefaultLocale());
				DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);

				// Initiating a text component for the kick message.
				final @NotNull TextComponent textComponent = Component.text(
						trs.getString("banMessageNoReason")
								.replace("%id%", ban.map(Ban::getBanID).orElse(""))
								.replace("%banStart%", dateFormat.format(ban.map(Ban::getBanStart))));

				// Kicking the player from the server (if present).
				t.ifPresent(player -> player.kick(textComponent, PlayerKickEvent.Cause.BANNED));
			}

			// Ban with reason.
			default -> {

				// Building the message string. Starting at args index 1 up to the length of args.
				StringBuilder stringBuilder = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					stringBuilder.append(args[i]).append(" ");
				}
				String reason = stringBuilder.toString().trim();

				// UUID of the targeted player.
				Optional<UUID> uuid = Optional.ofNullable(Bukkit.getPlayerUniqueId(args[0]));

				// Returning if the player isn't present.
				if (uuid.isEmpty()) {
					sender.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
					return true;
				}

				// Ban with and without punisher's uuid
				if (sender instanceof Player p) BanManager.ban(uuid.orElse(null), p.getUniqueId(), reason);
				else BanManager.ban(uuid.orElse(null), reason);

				// Sending the confirmation to the sender that the ban was successful.
				sender.sendMessage(
						rs.getString("banMessageSelf")
								.replace("%reason%", reason)
								.replace("%target%", Optional.ofNullable(
												Bukkit.getPlayer(uuid.orElse(null))).map(Player::getName)
										.orElseGet(() -> Optional.ofNullable(
												Bukkit.getOfflinePlayer(uuid.orElse(null)).getName()).orElse(""))));

				// Instantiating the latest ban.
				Optional<Ban> ban = BanManager.getLatestBan(uuid.orElse(null));

				// Instantiating the banned player.
				Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(uuid.orElse(null)));

				// Returning if the banned player is offline (not present).
				if (t.isEmpty()) return true;

				// Instantiating the player's resource bundle.
				ResourceBundle trs = LanguageHandler.getResourceBundle(uuid.orElse(null));

				// Instantiating the player's user object.
				Optional<User> user = User.getUser(uuid.orElse(null));

				// Instantiating the player's locale for correct date formatting.
				Locale locale = user.map(User::getLocale)
						.map(de.hiyamacity.objects.Locale::getJavaUtilLocale)
						.orElse(LanguageHandler.getDefaultLocale());
				DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);

				// Initiating a text component for the kick message.
				final @NotNull TextComponent textComponent = Component.text(trs.getString("banMessage").replace("%reason%", reason).replace("%id%", ban.map(Ban::getBanID).orElse("")).replace("%banStart%", dateFormat.format(ban.map(Ban::getBanStart))));

				// Kicking the player from the server (if present).
				t.ifPresent(player -> player.kick(textComponent, PlayerKickEvent.Cause.BANNED));
			}
		}
		return false;
	}
}
