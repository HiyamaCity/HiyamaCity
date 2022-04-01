package de.hiyamacity.commands.user;

import de.hiyamacity.misc.Distances;
import de.hiyamacity.util.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.ResourceBundle;

public class DiceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        int result = new Random().nextInt(6);
        for (Player all : Bukkit.getOnlinePlayers())
            if (p.getLocation().distanceSquared(all.getLocation()) <= Distances.CHAT_MESSAGE_NEAREST) {
                ResourceBundle resourceBundle = LanguageHandler.getResourceBundle(all.getUniqueId());
                all.sendMessage(resourceBundle.getString("dice").replace("%player%", p.getName()).replace("%result%", "" + result));
            }

        return false;
    }
}
