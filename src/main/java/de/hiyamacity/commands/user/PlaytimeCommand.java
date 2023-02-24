package de.hiyamacity.commands.user;

import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.entity.Playtime;
import de.hiyamacity.entity.User;
import de.hiyamacity.util.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class PlaytimeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if(!(sender instanceof Player p)) {
			ResourceBundle rs = LanguageHandler.getResourceBundle();
			sender.sendMessage(rs.getString("playerCommand"));
			return true;
		}
		
		final UUID uuid = p.getUniqueId();
		final ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);
		
		if(args.length > 1) {
			p.sendMessage(rs.getString("playtimeUsage"));
			return true;
		}

		if(args.length == 0) {
			final Optional<User> optionalUser = new UserDAOImpl().getUserByPlayerUniqueID(p.getUniqueId());

			if(optionalUser.isEmpty()) {
				p.sendMessage(rs.getString("userFetchFailed"));
				return true;
			}

			final User user = optionalUser.get();
			final Playtime playtime = user.getPlaytime();
			
			if(playtime == null) {
				p.sendMessage(rs.getString("userFetchFailed"));
				return true;
			}
			
			final long minutes = playtime.getMinutes();
			final long hours = playtime.getHours();

			String message = rs.getString("playtimeMessage");
			MessageFormat messageFormat = new MessageFormat(message, rs.getLocale());
			message = messageFormat.format(new Object[]{p.getName(), hours, minutes});
			p.sendMessage(message);
		} else {
			Optional<Player> targetOptional = Optional.ofNullable(Bukkit.getPlayer(args[0]));
			if(targetOptional.isEmpty()) {
				String message = rs.getString("playerNotFound");
				message = MessageFormat.format(message, args[0]);
				p.sendMessage(message);
				return true;
			}
			
			final Player targetPlayer = targetOptional.get();
			final Optional<User> optionalUser = new UserDAOImpl().getUserByPlayerUniqueID(targetPlayer.getUniqueId());
			
			if(optionalUser.isEmpty()) {
				p.sendMessage(rs.getString("userFetchFailed"));
				return true;
			}
			
			final User user = optionalUser.get();
			final Playtime playtime = user.getPlaytime();
			final long minutes = playtime.getMinutes();
			final long hours = playtime.getHours();
			
			String message = rs.getString("playtimeMessage");
			MessageFormat messageFormat = new MessageFormat(message, rs.getLocale());
			message = messageFormat.format(new Object[]{targetPlayer.getName(), hours, minutes});
			p.sendMessage(message);
		}
		
		return false;
	}
}
