package de.hiyamacity.commands.user;

import de.hiyamacity.misc.Distances;
import de.hiyamacity.lang.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

public class MeCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return true;
        ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
        if (args.length < 1) {
            p.sendMessage(rs.getString("meUsage"));
            return true;
        }

        String message = String.join(" ", args);
        if (!message.endsWith(".")) message = message + ".";

        for (Player t : Bukkit.getOnlinePlayers())
            if (p.getLocation().distanceSquared(t.getLocation()) <= Distances.CHAT_MESSAGE_NEAREST) {
                ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());
                t.sendMessage(trs.getString("me").replace("%player%", p.getName()).replace("%message%", message));
            }

        return false;
    }
}
