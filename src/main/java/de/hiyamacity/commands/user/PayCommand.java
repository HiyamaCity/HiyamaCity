package de.hiyamacity.commands.user;

import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.entity.User;
import de.hiyamacity.util.Distances;
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

public class PayCommand implements CommandExecutor {

	final double PAY_DISTANCE = Distances.KISS;
	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if(!(commandSender instanceof Player p)) {
			ResourceBundle rs = LanguageHandler.getResourceBundle();
			commandSender.sendMessage(rs.getString("playerCommand"));
			return true;
		}
		
		final UUID uuid = p.getUniqueId();
		final ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);
		
		if(args.length != 2) {
			p.sendMessage(rs.getString("payUsage"));
			return true;
		}
		
		final Optional<Player> targetOptional = Optional.ofNullable(Bukkit.getPlayer(args[0]));
		
		if(targetOptional.isEmpty()) {
			String message = rs.getString("playerNotFound");
			message = MessageFormat.format(message, args[0]);
			p.sendMessage(message);
			return true;
		}
		
		final Player target = targetOptional.get();
		
		if(p.getName().equals(target.getName())) {
			p.sendMessage(rs.getString("cantPaySelf"));
			return true;
		}
		
		if(p.getLocation().distanceSquared(target.getLocation()) > PAY_DISTANCE) {
			String message = rs.getString("playerTooFarAway");
			message = MessageFormat.format(message, target.getName(), Math.sqrt(PAY_DISTANCE));
			p.sendMessage(message);
			return true;
		}
		
		final long amount = Long.parseLong(args[1]);
		
		if(amount < 0) {
			p.sendMessage(rs.getString("payNonNegative"));
			return true;
		}

		final UserDAOImpl userDAO = new UserDAOImpl();
		final Optional<User> senderUser = userDAO.getUserByPlayerUniqueID(uuid);
		final Optional<User> targetUser = userDAO.getUserByPlayerUniqueID(target.getUniqueId());
		
		if(senderUser.isEmpty()) {
			String message = rs.getString("databasePlayerNotFound");
			message = MessageFormat.format(message, p.getName());
			p.sendMessage(message);
			return true;
		}

		if(targetUser.isEmpty()) {
			String message = rs.getString("databasePlayerNotFound");
			message = MessageFormat.format(message, target.getName());
			p.sendMessage(message);
			return true;
		}
		
		long senderAmount = senderUser.get().getPurse();
		
		if(senderAmount < amount) {
			p.sendMessage(rs.getString("payInsufficientFunds"));
			return true;
		}
		
		senderUser.ifPresent(user -> {
			user.setPurse(user.getPurse() - amount);
			userDAO.update(user);
			
			String message = rs.getString("paySend");
			message = MessageFormat.format(message, amount, rs.getString("currencySymbol"), target.getName());
			p.sendMessage(message);
		});
		
		targetUser.ifPresent(user -> {
			user.setPurse(user.getPurse() + amount);
			userDAO.update(user);

			ResourceBundle targetResourceBundle = LanguageHandler.getResourceBundle(target.getUniqueId());
			String message = targetResourceBundle.getString("payReceive");
			message = MessageFormat.format(message, p.getName(), amount, targetResourceBundle.getString("currencySymbol"));
			target.sendMessage(message);
		});
		
		return false;
	}
}
