package de.hiyamacity.commands.user;

import de.hiyamacity.util.LanguageHandler;
import org.bukkit.Bukkit;
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

        if (args.length < 2) {
            p.sendMessage(resourceBundle.getString("messageUsage"));
            return true;
        }

        Player t = Bukkit.getPlayer(args[0]);

        if (t == null) {
            p.sendMessage(resourceBundle.getString("playerNotFound").replace("%target%", args[0]));
            return true;
        }

        if (p.getName().equals(t.getName())) {
            p.sendMessage(resourceBundle.getString("messageNotToYourself"));
            return true;
        }

        //     0       1     2    3   4   5   6   7    8
        // /message <Name> Hallo ich bin cool du aber auch

        StringBuilder msg = new StringBuilder();

        // i + 1 -> args.length
        for (int i = 1; i < args.length; i++) {
            msg.append(args[i]).append(" ");
        }

        String message = msg.toString().trim();

        p.sendMessage(resourceBundle.getString("messageSelf").replace("%target%", t.getName()).replace("%msg%", message));
        ResourceBundle targetResourceBundle = LanguageHandler.getResourceBundle(t.getUniqueId());
        t.sendMessage(targetResourceBundle.getString("messageOther").replace("%player%", p.getName()).replace("%msg%", message));

        return false;
    }
}
