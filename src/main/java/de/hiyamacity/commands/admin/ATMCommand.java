package de.hiyamacity.commands.admin;

import de.hiyamacity.dao.ATMDAOImpl;
import de.hiyamacity.dao.LocationDAOImpl;
import de.hiyamacity.entity.ATM;
import de.hiyamacity.util.LanguageHandler;
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

public class ATMCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if(!(sender instanceof Player p)) {
			ResourceBundle rs = LanguageHandler.getResourceBundle();
			sender.sendMessage(rs.getString("playerCommand"));
			return true;
		}
		
		if(!p.hasPermission("admin")) return true;
		
		final UUID uuid = p.getUniqueId();
		final ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);
		
		if(args.length > 3 || args.length < 1) {
			p.sendMessage(rs.getString("atmAdminPlainUsage"));
			return true;
		}
		
		switch (args[0].toLowerCase()) {
			case "create" -> {
				if(args.length != 1) {
					p.sendMessage(rs.getString("atmAdminPlainUsage"));
					return true;
				}
				
				final ATMDAOImpl atmDAO = new ATMDAOImpl();
				final ATM atm = new ATM();
				final de.hiyamacity.entity.Location location = new de.hiyamacity.entity.Location().fromBukkitLocation(p.getEyeLocation().getBlock().getLocation());
				final LocationDAOImpl locationDAO = new LocationDAOImpl();
				locationDAO.create(location);
				
				atm.setLocation(location);
				atm.setAmount(150000);
				atm.setMaximumAmount(150000);
				atmDAO.create(atm);

				String message = rs.getString("atmAdminCreate");
				message = MessageFormat.format(message, atm.getId(), atm.getLocation().getX(), atm.getLocation().getY(), atm.getLocation().getZ());
				p.sendMessage(message);
				
			}
			case "delete" -> {
				if(args.length != 1) {
					p.sendMessage(rs.getString("atmAdminPlainUsage"));
					return true;
				}

				final Optional<ATM> currentATM = getLookingAtATM(p.getEyeLocation().getBlock().getLocation());
				final ATMDAOImpl atmDAO = new ATMDAOImpl();
				
				if(currentATM.isEmpty()) {
					p.sendMessage(rs.getString("atmNotFound"));
					return true;
				}
				
				final ATM atm = currentATM.get();
				atmDAO.delete(ATM.class, atm.getId());

				String message = rs.getString("atmAdminDelete");
				message = MessageFormat.format(message, atm.getId(), atm.getLocation().getX(), atm.getLocation().getY(), atm.getLocation().getZ());
				p.sendMessage(message);
				
			}
			case "modify" -> {
				if(args.length != 3) {
					p.sendMessage(rs.getString("atmAdminModifyUsage"));
					return true;
				}
				
				final Optional<ATM> currentATM = getLookingAtATM(p.getEyeLocation().getBlock().getLocation());

				if(!isLong(args[2])) {
					p.sendMessage(rs.getString("inputNaN"));
					return true;
				}
				
				final long amount = Long.parseLong(args[2]);
				final ATMDAOImpl atmDAO = new ATMDAOImpl();
				
				if(currentATM.isEmpty()) {
					p.sendMessage(rs.getString("atmNotFound"));
					return true;
				}

				final ATM atm = currentATM.get();
				final long oldAmount = atm.getAmount();
				final long oldMaximum = atm.getMaximumAmount();
				
				switch (args[1].toLowerCase()) {
					case "amount" -> atm.setAmount(amount);
					case "maximum" -> atm.setMaximumAmount(amount);
					default -> {
						p.sendMessage(rs.getString("atmAdminModifyUsage"));
						return true;
					}
				}
				
				atmDAO.update(atm);
				
				switch (args[1].toLowerCase()) {
					case "amount" -> {
						String message = rs.getString("atmAdminModify");
						MessageFormat messageFormat = new MessageFormat(message, rs.getLocale());
						message = messageFormat.format(new Object[]{atm.getId(), "amount", oldAmount, atm.getAmount()});
						p.sendMessage(message);
					}
					case "maximum" -> {
						String message = rs.getString("atmAdminModify");
						MessageFormat messageFormat = new MessageFormat(message, rs.getLocale());
						message = messageFormat.format(new Object[]{atm.getId(), "maximum", oldMaximum, atm.getMaximumAmount()});
						p.sendMessage(message);
					}
				}
			}
			default -> {
				p.sendMessage(rs.getString("atmAdminPlainUsage"));
				return true;
			}
		}
		
		return false;
	}
	
	private Optional<ATM> getLookingAtATM(@NotNull Location location) {
		ATMDAOImpl atmDAO = new ATMDAOImpl();
		List<ATM> atms = atmDAO.getATMs();
		for (ATM atm : atms) {
			if(atm.getLocation().toBukkitLocation().equals(location)) return Optional.of(atm);
		}
		return Optional.empty();
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if(args.length == 1) return List.of("create", "delete", "modify");
		else if(args.length == 2 || !args[0].contains("create") || !args[0].contains("delete")) return List.of("amount", "maximum");
		else return null;
	}
}
