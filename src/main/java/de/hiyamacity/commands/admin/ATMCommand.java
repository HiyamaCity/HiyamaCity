package de.hiyamacity.commands.admin;

import de.hiyamacity.dao.ATMDAOImpl;
import de.hiyamacity.dao.LocationDAOImpl;
import de.hiyamacity.jpa.ATM;
import de.hiyamacity.util.player.LanguageHandler;
import org.bukkit.Location;
import org.bukkit.block.data.type.WallSign;
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

public class ATMCommand implements CommandExecutor, TabCompleter {

	private static final String ATM_ADMIN_PLAIN_USAGE = "atmAdminPlainUsage";
	private static final String ATM_NOT_FOUND = "atmNotFound";
	private static final String CREATE_CASE = "create";
	private static final String DELETE_CASE = "delete";
	private static final String AMOUNT_CASE = "amount";
	private static final String MAXIMUM_CASE = "maximum";

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player p)) {
			ResourceBundle rs = LanguageHandler.getResourceBundle();
			sender.sendMessage(rs.getString("playerCommand"));
			return true;
		}

		if (!p.hasPermission("hiyamacity.commands.admin.atm")) return true;

		final UUID uuid = p.getUniqueId();
		final ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);

		if (args.length > 3 || args.length < 1) {
			p.sendMessage(rs.getString(ATM_ADMIN_PLAIN_USAGE));
			return true;
		}

		switch (args[0].toLowerCase()) {
			case CREATE_CASE -> {
				if (args.length != 1) {
					p.sendMessage(rs.getString(ATM_ADMIN_PLAIN_USAGE));
					return true;
				}

				final ATMDAOImpl atmDAO = new ATMDAOImpl();
				final ATM atm = new ATM();
				final de.hiyamacity.jpa.Location location = new de.hiyamacity.jpa.Location().fromBukkitLocation(getATMLocation(p));
				final LocationDAOImpl locationDAO = new LocationDAOImpl();

				if (!(location.toBukkitLocation().getBlock().getBlockData() instanceof WallSign)) {
					p.sendMessage(rs.getString("atmLookAtSign"));
					return true;
				}

				locationDAO.create(location);

				atm.setLocation(location);
				atm.setAmount(150000);
				atm.setMaximumAmount(150000);
				atmDAO.create(atm);

				String message = rs.getString("atmAdminCreate");
				message = MessageFormat.format(message, atm.getId(), atm.getLocation().getX(), atm.getLocation().getY(), atm.getLocation().getZ());
				p.sendMessage(message);

			}
			case DELETE_CASE -> {
				if (args.length != 1) {
					p.sendMessage(rs.getString(ATM_ADMIN_PLAIN_USAGE));
					return true;
				}

				final Optional<ATM> currentATM = getLookingAtATM(getATMLocation(p));
				final ATMDAOImpl atmDAO = new ATMDAOImpl();

				if (currentATM.isEmpty()) {
					p.sendMessage(rs.getString(ATM_NOT_FOUND));
					return true;
				}

				final ATM atm = currentATM.get();
				atmDAO.delete(ATM.class, atm.getId());

				String message = rs.getString("atmAdminDelete");
				message = MessageFormat.format(message, atm.getId(), atm.getLocation().getX(), atm.getLocation().getY(), atm.getLocation().getZ());
				p.sendMessage(message);

			}
			case "modify" -> {
				if (args.length != 3) {
					p.sendMessage(rs.getString("atmAdminModifyUsage"));
					return true;
				}

				final Optional<ATM> currentATM = getLookingAtATM(getATMLocation(p));

				if (!isLong(args[2])) {
					p.sendMessage(rs.getString("inputNaN"));
					return true;
				}

				final long amount = Long.parseLong(args[2]);
				final ATMDAOImpl atmDAO = new ATMDAOImpl();

				if (currentATM.isEmpty()) {
					p.sendMessage(rs.getString(ATM_NOT_FOUND));
					return true;
				}

				final ATM atm = currentATM.get();
				final long oldAmount = atm.getAmount();
				final long oldMaximum = atm.getMaximumAmount();

				switch (args[1].toLowerCase()) {
					case AMOUNT_CASE -> atm.setAmount(amount);
					case MAXIMUM_CASE -> atm.setMaximumAmount(amount);
					default -> {
						p.sendMessage(rs.getString("atmAdminModifyUsage"));
						return true;
					}
				}

				atmDAO.update(atm);

				switch (args[1].toLowerCase()) {
					case AMOUNT_CASE -> {
						String message = rs.getString("atmAdminModify");
						MessageFormat messageFormat = new MessageFormat(message, rs.getLocale());
						message = messageFormat.format(new Object[]{atm.getId(), AMOUNT_CASE, oldAmount, atm.getAmount()});
						p.sendMessage(message);
					}
					case MAXIMUM_CASE -> {
						String message = rs.getString("atmAdminModify");
						MessageFormat messageFormat = new MessageFormat(message, rs.getLocale());
						message = messageFormat.format(new Object[]{atm.getId(), MAXIMUM_CASE, oldMaximum, atm.getMaximumAmount()});
						p.sendMessage(message);
					}
					default -> {
						return true;
					}
				}
			}
			case "info" -> {
				final Optional<ATM> currentATM = getLookingAtATM(getATMLocation(p));

				if (currentATM.isEmpty()) {
					p.sendMessage(rs.getString(ATM_NOT_FOUND));
					return true;
				}

				final ATM atm = currentATM.get();
				p.sendMessage("\n" + atm + "\n ");
			}
			default -> {
				p.sendMessage(rs.getString(ATM_ADMIN_PLAIN_USAGE));
				return true;
			}
		}

		return false;
	}

	private Optional<ATM> getLookingAtATM(@NotNull Location location) {
		final ATMDAOImpl atmDAO = new ATMDAOImpl();
		final List<ATM> atms = atmDAO.findAll();

		for (ATM atm : atms)
			if (atm.getLocation().toBukkitLocation().equals(location)) return Optional.of(atm);

		return Optional.empty();
	}

	private Location getATMLocation(@NotNull Player p) {
		return p.getTargetBlock(null, 5).getLocation();
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (args.length == 1) return List.of(CREATE_CASE, DELETE_CASE, "modify", "info");
		else if (args.length == 2 || !args[0].contains(CREATE_CASE) || !args[0].contains(DELETE_CASE) || !args[0].contains("info"))
			return List.of(AMOUNT_CASE, MAXIMUM_CASE);
		else return null;
	}
}
