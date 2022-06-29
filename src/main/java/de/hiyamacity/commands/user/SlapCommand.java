package de.hiyamacity.commands.user;

import de.hiyamacity.misc.Distances;
import de.hiyamacity.lang.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.ResourceBundle;

public class SlapCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return true;
        ResourceBundle resourceBundle = LanguageHandler.getResourceBundle(p.getUniqueId());

        if (args.length != 1) {
            p.sendMessage(resourceBundle.getString("slapUsage"));
            return true;
        }

        Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(args[0]));

        if (t.isEmpty()) {
            p.sendMessage(resourceBundle.getString("playerNotFound").replace("%target%", args[0]));
            return true;
        }

        if (p.getLocation().distanceSquared(t.map(Player::getLocation).orElse(null)) > Distances.KISS) {
            p.sendMessage(resourceBundle.getString("playerTooFarAway"));
            return true;
        }

        if (p.getName().equals(t.map(Player::getName).orElse(null))) {
            p.sendMessage(resourceBundle.getString("slapSelf"));
            return true;
        }

        for (Player o : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().distanceSquared(t.map(Player::getLocation).orElse(null)) <= Distances.KISS) {
                ResourceBundle ors = LanguageHandler.getResourceBundle(o.getUniqueId());
                o.sendMessage(ors.getString("slap").replace("%player%", p.getName()).replace("%target%", t.map(Player::getName).orElse(null)));
            }
        }

        return false;
    }
}