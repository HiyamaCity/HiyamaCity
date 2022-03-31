package de.hiyamacity.commands.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender Sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(Sender instanceof Player)) {
            return true;
        }
        Player p = (Player) Sender;


        return false;
    }
}
