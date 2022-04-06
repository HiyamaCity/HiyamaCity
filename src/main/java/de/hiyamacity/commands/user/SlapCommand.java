package de.hiyamacity.commands.user;

import de.hiyamacity.misc.Distances;
import de.hiyamacity.util.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

public class SlapCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        ResourceBundle resourceBundle = LanguageHandler.getResourceBundle(p.getUniqueId());

        if (args.length != 1) {
            p.sendMessage(resourceBundle.getString("slapUsage"));
            return true;
        }
        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            p.sendMessage(resourceBundle.getString("playerNotFound").replace("%target%", args[0]));
            return true;
        }
        if (p.getLocation().distanceSquared(t.getLocation()) > Distances.KISS) {
            p.sendMessage(resourceBundle.getString("playerTooFarAway"));
            return true;
        }
        if (p.getName().equals(t.getName())) {
            p.sendMessage(resourceBundle.getString("slapSelf"));
            return true;
        }
        Location pLoc = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 2, p.getLocation().getZ());
        Location tLoc = new Location(t.getWorld(), t.getLocation().getX(), t.getLocation().getY() + 2, t.getLocation().getZ());

        for (Player o : Bukkit.getOnlinePlayers()) {


            if (p.getLocation().distanceSquared(t.getLocation()) <= Distances.KISS) {
                ResourceBundle ors = LanguageHandler.getResourceBundle(o.getUniqueId());
                o.sendMessage(ors.getString("slap").replace("%player%", p.getName()).replace("%target%", t.getName()));
            }
        }

        return false;
    }
}