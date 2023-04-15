package de.hiyamacity.commands.admin;

import de.hiyamacity.dao.AtmDAOImpl;
import de.hiyamacity.dao.LocationDAOImpl;
import de.hiyamacity.jpa.ATM;
import de.hiyamacity.util.player.LanguageHandler;
import net.kyori.adventure.text.Component;
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
			case CREATE_CASE -> handleAtmCreation(args, p, rs);
			case DELETE_CASE -> handleAtmDeletion(args, p, rs);
			case "modify" -> handleAtmModification(args, p, rs);
			case "info" -> handleAtmInfo(p, rs);
			default -> {
				p.sendMessage(rs.getString(ATM_ADMIN_PLAIN_USAGE));
				return true;
			}
		}

		return false;
	}

	private void handleAtmCreation(@NotNull String @NotNull [] args, Player p, ResourceBundle rs) {
		if (args.length != 1) {
			p.sendMessage(rs.getString(ATM_ADMIN_PLAIN_USAGE));
			return;
		}

		final AtmDAOImpl atmDAO = new AtmDAOImpl();
		final ATM atm = new ATM();
		final de.hiyamacity.jpa.Location location = new de.hiyamacity.jpa.Location().fromBukkitLocation(getATMLocation(p));
		final LocationDAOImpl locationDAO = new LocationDAOImpl();

		if (!(location.toBukkitLocation().getBlock().getBlockData() instanceof WallSign)) {
			p.sendMessage(rs.getString("atmLookAtSign"));
			return;
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

	private void handleAtmDeletion(@NotNull String @NotNull [] args, Player p, ResourceBundle rs) {
		if (args.length != 1) {
			p.sendMessage(rs.getString(ATM_ADMIN_PLAIN_USAGE));
			return;
		}

		final Optional<ATM> currentATM = getLookingAtATM(getATMLocation(p));
		final AtmDAOImpl atmDAO = new AtmDAOImpl();

		if (currentATM.isEmpty()) {
			p.sendMessage(rs.getString(ATM_NOT_FOUND));
			return;
		}

		final ATM atm = currentATM.get();
		atmDAO.delete(ATM.class, atm.getId());

		String message = rs.getString("atmAdminDelete");
		message = MessageFormat.format(message, atm.getId(), atm.getLocation().getX(), atm.getLocation().getY(), atm.getLocation().getZ());
		p.sendMessage(message);
	}

	private void handleAtmModification(@NotNull String @NotNull [] args, Player p, ResourceBundle rs) {
		if (args.length != 3) {
			p.sendMessage(rs.getString("atmAdminModifyUsage"));
			return;
		}

		final Optional<ATM> currentATM = getLookingAtATM(getATMLocation(p));

		if (!isLong(args[2])) {
			p.sendMessage(rs.getString("inputNaN"));
			return;
		}

		final long amount = Long.parseLong(args[2]);
		final AtmDAOImpl atmDAO = new AtmDAOImpl();

		if (currentATM.isEmpty()) {
			p.sendMessage(rs.getString(ATM_NOT_FOUND));
			return;
		}

		final ATM atm = currentATM.get();
		final long oldAmount = atm.getAmount();
		final long oldMaximum = atm.getMaximumAmount();

		switch (args[1].toLowerCase()) {
			case AMOUNT_CASE -> atm.setAmount(amount);
			case MAXIMUM_CASE -> atm.setMaximumAmount(amount);
			default -> {
				p.sendMessage(rs.getString("atmAdminModifyUsage"));
				return;
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
			default -> p.sendMessage(rs.getString("atm.action.not_found"));
		}
	}

	private void handleAtmInfo(Player p, ResourceBundle rs) {
		final Optional<ATM> currentATM = getLookingAtATM(getATMLocation(p));

		if (currentATM.isEmpty()) {
			p.sendMessage(rs.getString(ATM_NOT_FOUND));
			return;
		}

		final ATM atm = currentATM.get();
		p.sendMessage(Component.text("\n" + atm + "\n"));
	}

	private Optional<ATM> getLookingAtATM(@NotNull Location location) {
		final AtmDAOImpl atmDAO = new AtmDAOImpl();
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
