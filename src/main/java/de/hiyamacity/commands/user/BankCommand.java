package de.hiyamacity.commands.user;

import de.hiyamacity.dao.ATMDAOImpl;
import de.hiyamacity.dao.BankAccountDAOImpl;
import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.entity.ATM;
import de.hiyamacity.entity.BankAccount;
import de.hiyamacity.entity.User;
import de.hiyamacity.util.Distances;
import de.hiyamacity.util.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import static de.hiyamacity.util.Util.isLong;

public class BankCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player p)) {
			ResourceBundle rs = LanguageHandler.getResourceBundle();
			sender.sendMessage(rs.getString("playerCommand"));
			return true;
		}

		final UUID uuid = p.getUniqueId();
		final ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);

		if (args.length > 3 || args.length < 1) {
			p.sendMessage(rs.getString("bankUsage"));
			return true;
		}

		final Optional<ATM> nearestATM = getNearestValidATM(p.getEyeLocation());
		final Optional<User> optionalUser = new UserDAOImpl().getUserByPlayerUniqueId(uuid);

		if (nearestATM.isEmpty()) {
			p.sendMessage(rs.getString("atmNotFound"));
			return true;
		}

		if (optionalUser.isEmpty()) {
			p.sendMessage(rs.getString("userFetchFailed"));
			return true;
		}

		final User user = optionalUser.get();
		BankAccountDAOImpl bankAccountDAO = new BankAccountDAOImpl();
		UserDAOImpl userDAO = new UserDAOImpl();
		BankAccount bankAccount = new BankAccount();

		if (user.getBankAccount() == null) {
			bankAccount.setAmount(4000);
			bankAccountDAO.create(bankAccount);
			user.setBankAccount(bankAccount);
			userDAO.update(user);
		}

		bankAccount = user.getBankAccount();
		final ATM atm = nearestATM.get();

		switch (args[0].toLowerCase()) {
			case "abbuchen" -> {
				if (args.length != 2) {
					p.sendMessage(rs.getString("bankWithdrawDepositUsage"));
					return true;
				}

				if (!isLong(args[1])) {
					p.sendMessage(rs.getString("inputNaN"));
					return true;
				}

				final long amount = Long.parseLong(args[1]);
				final long atmAmount = atm.getAmount();
				final long userAmount = user.getPurse();

				if (amount <= 0) {
					p.sendMessage(rs.getString("bankNonNegative"));
					return true;
				}

				if (atmAmount < amount) {
					p.sendMessage(rs.getString("bankWithdrawNotEnoughMoneyInATM"));
					return true;
				}

				if (bankAccount.getAmount() < amount) {
					p.sendMessage(rs.getString("bankWithdrawNotEnoughMoneyOnBank"));
					return true;
				}

				bankAccount.setAmount(bankAccount.getAmount() - amount);
				atm.setAmount(atmAmount - amount);
				user.setPurse(userAmount + amount);

				bankAccountDAO.update(bankAccount);
				new ATMDAOImpl().update(atm);
				userDAO.update(user);

				String message = rs.getString("bankWithdraw");
				MessageFormat messageFormat = new MessageFormat(message, rs.getLocale());
				message = messageFormat.format(new Object[]{amount, rs.getString("currencySymbol")});
				p.sendMessage(message);
			}
			case "einzahlen" -> {
				if (args.length != 2) {
					p.sendMessage(rs.getString("bankWithdrawDepositUsage"));
					return true;
				}

				if (!isLong(args[1])) {
					p.sendMessage(rs.getString("inputNaN"));
					return true;
				}

				long amount = Long.parseLong(args[1]);
				final long atmAmount = atm.getAmount();
				final long atmMaximum = atm.getMaximumAmount();
				final long userAmount = user.getPurse();

				if (amount <= 0) {
					p.sendMessage(rs.getString("bankNonNegative"));
					return true;
				}

				if (userAmount < amount) {
					p.sendMessage(rs.getString("payInsufficientFunds"));
					return true;
				}

				long oldAmount = amount;
				if (atmMaximum < atmAmount + amount) {
					amount = atmMaximum - atmAmount;
				}

				bankAccount.setAmount(bankAccount.getAmount() + oldAmount);
				user.setPurse(userAmount - oldAmount);
				atm.setAmount(atmAmount + amount);

				bankAccountDAO.update(bankAccount);
				new ATMDAOImpl().update(atm);
				userDAO.update(user);

				String message = rs.getString("bankDeposit");
				MessageFormat messageFormat = new MessageFormat(message, rs.getLocale());
				message = messageFormat.format(new Object[]{oldAmount, rs.getString("currencySymbol")});
				p.sendMessage(message);
			}
			case "info" -> {
				if (args.length != 1) {
					p.sendMessage(rs.getString("bankInfoUsage"));
					return true;
				}

				String message = rs.getString("bankInfo");
				MessageFormat messageFormat = new MessageFormat(message, rs.getLocale());
				message = messageFormat.format(new Object[]{p.getName(), bankAccount.getAmount(), rs.getString("currencySymbol"), atm.getAmount(), rs.getString("currencySymbol")});
				p.sendMessage(message);
			}
			case "überweisen" -> {
				if (args.length != 3) {
					p.sendMessage(rs.getString("bankTransferUsage"));
					return true;
				}

				if (!isLong(args[2])) {
					p.sendMessage(rs.getString("inputNaN"));
					return true;
				}

				final Optional<Player> targetOptional = Optional.ofNullable(Bukkit.getPlayer(args[1]));

				if (targetOptional.isEmpty()) {
					String message = rs.getString("playerNotFound");
					message = MessageFormat.format(message, args[1]);
					p.sendMessage(message);
					return true;
				}

				final Player target = targetOptional.get();

				if (p.getName().equals(target.getName())) {
					p.sendMessage(rs.getString("bankTransferSelf"));
					return true;
				}

				final Optional<User> optionalTargetUser = userDAO.getUserByPlayerUniqueId(target.getUniqueId());

				if (optionalTargetUser.isEmpty()) {
					p.sendMessage(rs.getString("userFetchFailed"));
					return true;
				}

				final User targetUser = optionalTargetUser.get();
				final long amount = Long.parseLong(args[2]);
				final BankAccount userBankAccount = user.getBankAccount();
				final BankAccount targetBankAccount = targetUser.getBankAccount();
				final long targetBankAmount = targetBankAccount.getAmount();
				final long userBankAmount = userBankAccount.getAmount();

				if (amount <= 0) {
					p.sendMessage(rs.getString("bankNonNegative"));
					return true;
				}

				if (userBankAmount < amount) {
					p.sendMessage(rs.getString("payInsufficientFunds"));
					return true;
				}

				userBankAccount.setAmount(userBankAmount - amount);
				bankAccountDAO.update(userBankAccount);
				targetBankAccount.setAmount(targetBankAmount + amount);
				bankAccountDAO.update(targetBankAccount);

				String message = rs.getString("bankTransferSend");
				MessageFormat messageFormat = new MessageFormat(message, rs.getLocale());
				message = messageFormat.format(new Object[]{amount, rs.getString("currencySymbol"), target.getName()});
				p.sendMessage(message);

				final ResourceBundle trs = LanguageHandler.getResourceBundle(target.getUniqueId());
				message = rs.getString("bankTransferReceive");
				MessageFormat format = new MessageFormat(message, trs.getLocale());
				message = format.format(new Object[]{p.getName(), amount, trs.getString("currencySymbol")});
				target.sendMessage(message);

			}
		}

		return false;
	}

	private Optional<ATM> getNearestValidATM(@NotNull Location location) {
		List<ATM> atms = new ATMDAOImpl().findAll();
		for (ATM atm : atms) {
			if (atm.getLocation().toBukkitLocation().distanceSquared(location) <= Distances.ATM_DISTANCE)
				return Optional.of(atm);
		}
		return Optional.empty();
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (args.length == 1) return List.of("abbuchen", "einzahlen", "info", "überweisen");
		return null;
	}
}
