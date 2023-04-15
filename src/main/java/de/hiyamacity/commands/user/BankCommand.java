package de.hiyamacity.commands.user;

import de.hiyamacity.dao.AtmDAOImpl;
import de.hiyamacity.dao.BankAccountDAOImpl;
import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.jpa.ATM;
import de.hiyamacity.jpa.BankAccount;
import de.hiyamacity.jpa.User;
import de.hiyamacity.util.Distance;
import de.hiyamacity.util.player.LanguageHandler;
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

	private static final String CURRENCY_SYMBOL = "currencySymbol";
	private static final String BANK_NON_NEGATIVE = "bankNonNegative";
	private static final String INPUT_NAN = "inputNaN";
	private static final BankAccountDAOImpl bankAccountDAO = new BankAccountDAOImpl();
	private static final UserDAOImpl userDAO = new UserDAOImpl();

	private static void handleBankTransfer(@NotNull String @NotNull [] args, Player p, ResourceBundle rs, User user) {
		if (args.length != 3) {
			p.sendMessage(rs.getString("bankTransferUsage"));
			return;
		}

		if (!isLong(args[2])) {
			p.sendMessage(rs.getString(INPUT_NAN));
			return;
		}

		final Optional<Player> targetOptional = Optional.ofNullable(Bukkit.getPlayer(args[1]));

		if (targetOptional.isEmpty()) {
			String message = rs.getString("playerNotFound");
			message = MessageFormat.format(message, args[1]);
			p.sendMessage(message);
			return;
		}

		final Player target = targetOptional.get();

		if (p.getName().equals(target.getName())) {
			p.sendMessage(rs.getString("bankTransferSelf"));
			return;
		}

		final Optional<User> optionalTargetUser = BankCommand.userDAO.getUserByPlayerUniqueId(target.getUniqueId());

		if (optionalTargetUser.isEmpty()) {
			p.sendMessage(rs.getString("userFetchFailed"));
			return;
		}

		final User targetUser = optionalTargetUser.get();
		final long amount = Long.parseLong(args[2]);
		final BankAccount userBankAccount = user.getBankAccount();
		final BankAccount targetBankAccount = targetUser.getBankAccount();
		final long targetBankAmount = targetBankAccount.getAmount();
		final long userBankAmount = userBankAccount.getAmount();

		if (amount <= 0) {
			p.sendMessage(rs.getString(BANK_NON_NEGATIVE));
			return;
		}

		if (userBankAmount < amount) {
			p.sendMessage(rs.getString("payInsufficientFunds"));
			return;
		}

		userBankAccount.setAmount(userBankAmount - amount);
		BankCommand.bankAccountDAO.update(userBankAccount);
		targetBankAccount.setAmount(targetBankAmount + amount);
		BankCommand.bankAccountDAO.update(targetBankAccount);

		String message = rs.getString("bankTransferSend");
		MessageFormat messageFormat = new MessageFormat(message, rs.getLocale());
		message = messageFormat.format(new Object[]{amount, rs.getString(CURRENCY_SYMBOL), target.getName()});
		p.sendMessage(message);

		final ResourceBundle trs = LanguageHandler.getResourceBundle(target.getUniqueId());
		message = rs.getString("bankTransferReceive");
		MessageFormat format = new MessageFormat(message, trs.getLocale());
		message = format.format(new Object[]{p.getName(), amount, trs.getString(CURRENCY_SYMBOL)});
		target.sendMessage(message);
	}

	private static void handleAtmInfo(@NotNull String @NotNull [] args, Player p, ResourceBundle rs, BankAccount bankAccount, ATM atm) {
		if (args.length != 1) {
			p.sendMessage(rs.getString("bankInfoUsage"));
			return;
		}

		String message = rs.getString("bankInfo");
		MessageFormat messageFormat = new MessageFormat(message, rs.getLocale());
		message = messageFormat.format(new Object[]{p.getName(), bankAccount.getAmount(), rs.getString(CURRENCY_SYMBOL), atm.getAmount(), rs.getString(CURRENCY_SYMBOL)});
		p.sendMessage(message);
	}

	private static void handleBankDeposit(@NotNull String @NotNull [] args, Player p, ResourceBundle rs, User user, BankAccount bankAccount, ATM atm) {
		if (args.length != 2) {
			p.sendMessage(rs.getString("bankWithdrawDepositUsage"));
			return;
		}

		if (!isLong(args[1])) {
			p.sendMessage(rs.getString(INPUT_NAN));
			return;
		}

		long amount = Long.parseLong(args[1]);
		final long atmAmount = atm.getAmount();
		final long atmMaximum = atm.getMaximumAmount();
		final long userAmount = user.getPurse();

		if (amount <= 0) {
			p.sendMessage(rs.getString(BANK_NON_NEGATIVE));
			return;
		}

		if (userAmount < amount) {
			p.sendMessage(rs.getString("payInsufficientFunds"));
			return;
		}

		long oldAmount = amount;
		if (atmMaximum < atmAmount + amount) {
			amount = atmMaximum - atmAmount;
		}

		bankAccount.setAmount(bankAccount.getAmount() + oldAmount);
		user.setPurse(userAmount - oldAmount);
		atm.setAmount(atmAmount + amount);

		BankCommand.bankAccountDAO.update(bankAccount);
		new AtmDAOImpl().update(atm);
		BankCommand.userDAO.update(user);

		String message = rs.getString("bankDeposit");
		MessageFormat messageFormat = new MessageFormat(message, rs.getLocale());
		message = messageFormat.format(new Object[]{oldAmount, rs.getString(CURRENCY_SYMBOL)});
		p.sendMessage(message);
	}

	private static void handleBankWithdraw(@NotNull String @NotNull [] args, Player p, ResourceBundle rs, User user, BankAccount bankAccount, ATM atm) {
		if (args.length != 2) {
			p.sendMessage(rs.getString("bankWithdrawDepositUsage"));
			return;
		}

		if (!isLong(args[1])) {
			p.sendMessage(rs.getString(INPUT_NAN));
			return;
		}

		final long amount = Long.parseLong(args[1]);
		final long atmAmount = atm.getAmount();
		final long userAmount = user.getPurse();

		if (amount <= 0) {
			p.sendMessage(rs.getString(BANK_NON_NEGATIVE));
			return;
		}

		if (atmAmount < amount) {
			p.sendMessage(rs.getString("bankWithdrawNotEnoughMoneyInATM"));
			return;
		}

		if (bankAccount.getAmount() < amount) {
			p.sendMessage(rs.getString("bankWithdrawNotEnoughMoneyOnBank"));
			return;
		}

		bankAccount.setAmount(bankAccount.getAmount() - amount);
		atm.setAmount(atmAmount - amount);
		user.setPurse(userAmount + amount);

		BankCommand.bankAccountDAO.update(bankAccount);
		new AtmDAOImpl().update(atm);
		BankCommand.userDAO.update(user);

		String message = rs.getString("bankWithdraw");
		MessageFormat messageFormat = new MessageFormat(message, rs.getLocale());
		message = messageFormat.format(new Object[]{amount, rs.getString(CURRENCY_SYMBOL)});
		p.sendMessage(message);
	}

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
			case "abbuchen" -> handleBankWithdraw(args, p, rs, user, bankAccount, atm);
			case "einzahlen" -> handleBankDeposit(args, p, rs, user, bankAccount, atm);
			case "info" -> handleAtmInfo(args, p, rs, bankAccount, atm);
			case "überweisen" -> handleBankTransfer(args, p, rs, user);

			default -> {
				p.sendMessage(rs.getString("atm.action.not_found"));
				return true;
			}

		}

		return false;
	}

	private Optional<ATM> getNearestValidATM(@NotNull Location location) {
		List<ATM> atms = new AtmDAOImpl().findAll();
		for (ATM atm : atms) {
			if (atm.getLocation().toBukkitLocation().distanceSquared(location) <= Distance.ATM.getValue())
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
