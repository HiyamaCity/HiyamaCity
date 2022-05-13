package de.hiyamacity.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AllCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return true;
        if (!p.hasPermission("allChat")) return true;
        String msg = String.join(" ", args);
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("@ALL " + p.getName() + ": " + msg));
        return false;
    }
}
