package de.hiyamacity.commands.user;

import de.hiyamacity.dao.ATMDAOImpl;
import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.entity.ATM;
import de.hiyamacity.entity.User;
import de.hiyamacity.util.Distances;
import de.hiyamacity.util.LanguageHandler;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class BankCommand implements CommandExecutor, TabCompleter {
	
	// TODO: Add user bank account and maximum withdraw amount.
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if(!(sender instanceof Player p)) {
			ResourceBundle rs = LanguageHandler.getResourceBundle();
			sender.sendMessage(rs.getString("playerCommand"));
			return true;
		}

		final UUID uuid = p.getUniqueId();
		final ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);

		if(args.length > 3 || args.length < 1) {
			p.sendMessage(rs.getString("bankUsage"));
			return true;
		}
		
		final Optional<ATM> nearestATM = getNearestValidATM(p.getEyeLocation());
		final Optional<User> optionalUser = new UserDAOImpl().getUserByPlayerUniqueID(uuid);
		
		if(nearestATM.isEmpty()) {
			p.sendMessage(rs.getString("atmNotFound"));
			return true;
		}
		
		if(optionalUser.isEmpty()) {
			p.sendMessage(rs.getString("userFetchFailed"));
			return true;
		}
		
		final User user = optionalUser.get();
		final ATM atm = nearestATM.get();
		
		switch (args[0].toLowerCase()) {
			case "abbuchen" -> {
				if(args.length != 2) {
					p.sendMessage("bankWithdrawDepositUsage");
					return true;
				}
				
				final long amount = Long.parseLong(args[1]);
				final long atmAmount = atm.getAmount();
				final long userAmount = user.getPurse();
				
				if(atmAmount <= amount) {
					p.sendMessage(rs.getString("bankWithdrawNotEnoughMoneyInATM"));
					return true;
				}
				
				atm.setAmount(atmAmount - amount);
				user.setPurse(userAmount + amount);
				
				new ATMDAOImpl().update(atm);
				new UserDAOImpl().update(user);
			}
			case "einzahlen" -> {
				if(args.length != 2) {
					p.sendMessage("bankWithdrawDepositUsage");
					return true;
				}

				long amount = Long.parseLong(args[1]);
				final long atmAmount = atm.getAmount();
				final long atmMaximum = atm.getMaximumAmount();
				final long userAmount = user.getPurse();

				if(userAmount < amount) {
					p.sendMessage(rs.getString("payInsufficientFunds"));
					return true;
				}
				
				if(atmMaximum < atmAmount + amount) {
					amount = atmMaximum - atmAmount;
				}

				atm.setAmount(atmAmount + amount);
				user.setPurse(userAmount - amount);

				new ATMDAOImpl().update(atm);
				new UserDAOImpl().update(user);
				
			}
			case "info" -> {
				
			}
			case "überweisen" -> {
				
			}
		}
		
		return false;
	}
	
	private Optional<ATM> getNearestValidATM(@NotNull Location location) {
		List<ATM> atms = new ATMDAOImpl().getATMs();
		for (ATM atm : atms) {
			if(atm.getLocation().toBukkitLocation().distanceSquared(location) <= Distances.ATM_DISTANCE) return Optional.of(atm);
		}
		return Optional.empty();
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if(args.length == 1) return List.of("abbuchen", "einzahlen", "info", "überweisen");
		return null;
	}
}
