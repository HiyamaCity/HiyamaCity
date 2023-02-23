package de.hiyamacity.commands.user;

import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.entity.User;
import de.hiyamacity.util.LanguageHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class MoneyCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player p)) {
			ResourceBundle rs = LanguageHandler.getResourceBundle();
			sender.sendMessage(rs.getString("playerCommand"));
			return true;
		}

		final UUID uuid = p.getUniqueId();
		final ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);
		final UserDAOImpl userDAO = new UserDAOImpl();
		Optional<User> userOptional = userDAO.getUserByPlayerUniqueID(uuid);

		if (userOptional.isEmpty()) {
			p.sendMessage(rs.getString("userFetchFailed"));
			return true;
		}

		final long number = userOptional.get().getPurse();
		String message = rs.getString("printMoney");
		MessageFormat messageFormat = new MessageFormat(message, userOptional.get().getLocale());
		message = messageFormat.format(new Object[]{number, rs.getString("currencySymbol")});
		p.sendMessage(message);

		return false;
	}
}
