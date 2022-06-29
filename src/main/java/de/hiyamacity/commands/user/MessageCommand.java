package de.hiyamacity.commands.user;

import de.hiyamacity.lang.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.ResourceBundle;

public class MessageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return true;
        ResourceBundle resourceBundle = LanguageHandler.getResourceBundle(p.getUniqueId());

        if (args.length < 2) {
            p.sendMessage(resourceBundle.getString("messageUsage"));
            return true;
        }

        Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(args[0]));

        if (t.isEmpty()) {
            p.sendMessage(resourceBundle.getString("playerNotFound").replace("%target%", args[0]));
            return true;
        }

        if (p.getName().equals(t.map(Player::getName).orElse(null))) {
            p.sendMessage(resourceBundle.getString("messageNotToYourself"));
            return true;
        }


        StringBuilder msg = new StringBuilder();

        // i + 1 -> args.length
        for (int i = 1; i < args.length; i++) {
            msg.append(args[i]).append(" ");
        }

        String message = msg.toString().trim();

        p.sendMessage(resourceBundle.getString("messageSelf").replace("%target%", t.map(Player::getName).orElse(null)).replace("%msg%", message));
        ResourceBundle targetResourceBundle = LanguageHandler.getResourceBundle(t.map(Player::getUniqueId).orElse(null));
        t.ifPresent(player -> player.sendMessage(targetResourceBundle.getString("messageOther").replace("%player%", p.getName()).replace("%msg%", message)));

        return false;
    }
}
