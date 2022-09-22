package de.hiyamacity.commands.user;

import de.hiyamacity.lang.LanguageHandler;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.ResourceBundle;

public class HealCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player p)) return true;
		ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
		Optional<AttributeInstance> attribute = Optional.ofNullable(p.getAttribute(Attribute.GENERIC_MAX_HEALTH));
		attribute.ifPresent(attributeInstance -> p.setHealth(attributeInstance.getValue()));
		p.setFoodLevel(20);
		p.sendMessage(rs.getString("healSelf"));
		return false;
	}
}
