package de.hiyamacity.commands.user;

import de.hiyamacity.misc.Distances;
import de.hiyamacity.lang.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

public class KissCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

        if (args.length != 1) {
            p.sendMessage(rs.getString("kissUsage"));
            return true;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
            return true;
        }

        if (p.getLocation().distanceSquared(t.getLocation()) > Distances.KISS) {
            p.sendMessage(rs.getString("playerTooFarAway"));
            return true;
        }

        if (p.getName().equals(t.getName())) {
            p.sendMessage(rs.getString("kissSelf"));
            return true;
        }

        Location pLoc = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 2, p.getLocation().getZ());
        Location tLoc = new Location(t.getWorld(), t.getLocation().getX(), t.getLocation().getY() + 2, t.getLocation().getZ());


        for (Player o : Bukkit.getOnlinePlayers()) {
            o.spawnParticle(Particle.HEART, pLoc, 1);
            o.spawnParticle(Particle.HEART, tLoc, 1);
            if (p.getLocation().distanceSquared(t.getLocation()) <= Distances.KISS) {
                ResourceBundle ors = LanguageHandler.getResourceBundle(o.getUniqueId());
                o.sendMessage(ors.getString("kiss").replace("%player%", p.getName()).replace("%target%", t.getName()));
            }
        }

        return false;
    }
}
