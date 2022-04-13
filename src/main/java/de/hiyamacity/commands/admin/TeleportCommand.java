package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.util.VanishHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

public class TeleportCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return true;
        ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
        if ((args.length == 0 || args.length > 4)) {
            p.sendMessage(rs.getString("teleportUsage"));
            return true;
        }
        if (!p.hasPermission("teleport")) return true;
        switch (args.length) {
            case 1 -> {
                Player t = Bukkit.getPlayer(args[0]);

                if (t == null) {
                    p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
                    return true;
                }

                ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());
                p.teleport(t.getLocation());

                p.sendMessage(rs.getString("teleportToOtherSelf").replace("%target%", t.getName()));
                if (!VanishHandler.isVanish(p))
                    t.sendMessage(trs.getString("teleportToOtherOther").replace("%player%", p.getName()));
                return true;
            }
            case 2 -> {
                Player t = Bukkit.getPlayer(args[0]);
                Player t1 = Bukkit.getPlayer(args[1]);
                if (t == null) {
                    p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
                    return true;
                }

                if (t1 == null) {
                    p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[1]));
                    return true;
                }

                t.teleport(t1.getLocation());
                ResourceBundle t1rs = LanguageHandler.getResourceBundle(t.getUniqueId());
                if (!p.equals(t1)) {
                    ResourceBundle t2rs = LanguageHandler.getResourceBundle(t1.getUniqueId());

                    p.sendMessage(rs.getString("teleportOtherToOtherSelf").replace("%target%", t.getName()).replace("target1", t1.getName()));
                    t.sendMessage(t1rs.getString("teleportOtherToOtherOther").replace("%player%", p.getName()).replace("%target1%", t1.getName()));
                    t1.sendMessage(t2rs.getString("teleportOtherToOtherOther1").replace("%player%", p.getName()).replace("%target1%", t1.getName()));
                } else {
                    p.sendMessage(rs.getString("teleportToOtherSelf").replace("%target%", t.getName()));
                    t.sendMessage(t1rs.getString("teleportToOtherOther").replace("%player%", p.getName()));

                }
                return true;
            }
            case 3 -> {
                Location loc = (isDouble(args[0]) && isDouble(args[1]) && isDouble(args[2]) ? new Location(p.getWorld(), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2])) : getRelativeLocation(p.getLocation(), args[0], args[1], args[2]));
                if (loc == null) return true;
                p.teleport(loc);
                p.sendMessage(rs.getString("teleportToCoordinates").replace("%x%", "" + loc.getX()).replace("%y%", "" + loc.getY()).replace("%z%", "" + loc.getZ()));
                return true;
            }
            case 4 -> {
                Location loc = (isDouble(args[1]) && isDouble(args[2]) && isDouble(args[3]) ? new Location(p.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3])) : getRelativeLocation(p.getLocation(), args[1], args[2], args[3]));
                if (loc == null) return true;
                Player t = Bukkit.getPlayer(args[0]);
                if (t == null) return true;
                t.teleport(loc);
                ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());
                p.sendMessage(rs.getString("teleportToCoordinatesOtherSelf").replace("%target%", t.getName()).replace("%x%", "" + loc.getX()).replace("%y%", "" + loc.getY()).replace("%z%", "" + loc.getZ()));
                t.sendMessage(trs.getString("teleportToCoordinatesOtherOther").replace("%player%", p.getName()).replace("%x%", "" + loc.getX()).replace("%y%", "" + loc.getY()).replace("%z%", "" + loc.getZ()));
                return true;
            }
            default -> {
                p.sendMessage(rs.getString("teleportUsage"));
                return true;
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return null;
        switch (args.length) {
            case 1 -> {
                Collection<? extends Player> playerCollection = Bukkit.getOnlinePlayers();
                playerCollection.forEach(player -> {
                    if (VanishHandler.isVanish(player)) playerCollection.remove(player);
                });
                List<String> playerNames = new ArrayList<>();
                for (Player col : playerCollection)
                    playerNames.add(col.getName());
                return playerNames;
            }
            case 2 -> {
                return List.of(String.valueOf(p.getTargetBlock(null, 100).getLocation().getX()));
            }
            case 3 -> {
                return List.of(String.valueOf(p.getTargetBlock(null, 100).getLocation().getY()));
            }
            case 4 -> {
                return List.of(String.valueOf(p.getTargetBlock(null, 100).getLocation().getZ()));
            }
            default -> {
            }
        }
        return null;
    }

    private Location getRelativeLocation(Location loc, String arg0, String arg1, String arg2) {
        if (!(arg0.startsWith("~") && arg1.startsWith("~") && arg2.startsWith("~"))) return null;
        return new Location(loc.getWorld(), Double.parseDouble(arg0.substring(1)) + loc.getX(), Double.parseDouble(arg1.substring(1)) + loc.getY(), Double.parseDouble(arg2.substring(1)) + loc.getZ());
    }

    private boolean isDouble(String s) {
        if (s == null) return false;
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
