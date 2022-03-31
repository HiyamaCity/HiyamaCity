package de.hiyamacity.commands.user;

import de.hiyamacity.util.LanguageHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

public class MessageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender Sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(Sender instanceof Player)) return true;
        Player p = (Player) Sender;
        ResourceBundle resourceBundle = LanguageHandler.getResourceBundle(p.getUniqueId());

        if (args.length != 2) {
            p.sendMessage(resourceBundle.getString("messageUsage"));
            return true;
        }

        return false;
    }
}
