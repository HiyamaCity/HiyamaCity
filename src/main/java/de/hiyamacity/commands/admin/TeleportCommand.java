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

import java.util.*;

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
                Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(args[0]));

                if (t.isEmpty()) {
                    p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
                    return true;
                }

                ResourceBundle trs = LanguageHandler.getResourceBundle(t.map(Player::getUniqueId).orElse(null));
                p.teleport(t.map(Player::getLocation).orElse(null));

                p.sendMessage(rs.getString("teleportToOtherSelf").replace("%target%", t.map(Player::getName).orElse("")));
                if (!VanishHandler.isVanish(p))
                    t.ifPresent(player -> player.sendMessage(trs.getString("teleportToOtherOther").replace("%player%", p.getName())));
                return true;
            }
            case 2 -> {
                Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(args[0]));
                Optional<Player> t1 = Optional.ofNullable(Bukkit.getPlayer(args[1]));

                if (t.isEmpty()) {
                    p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
                    return true;
                }

                if (t1.isEmpty()) {
                    p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[1]));
                    return true;
                }

                t.ifPresent(player -> player.teleport(t1.map(Player::getLocation).orElse(null)));
                ResourceBundle trs = LanguageHandler.getResourceBundle(t.map(Player::getUniqueId).orElse(null));
                if (!p.equals(t1.orElse(null))) {
                    ResourceBundle t1rs = LanguageHandler.getResourceBundle(t1.map(Player::getUniqueId).orElse(null));

                    p.sendMessage(rs.getString("teleportOtherToOtherSelf").replace("%target%", t.map(Player::getName).orElse("")).replace("target1", t1.map(Player::getName).orElse("")));
                    t.ifPresent(player -> player.sendMessage(trs.getString("teleportOtherToOtherOther").replace("%player%", p.getName()).replace("%target1%", t1.map(Player::getName).orElse(""))));
                    t1.ifPresent(player -> player.sendMessage(t1rs.getString("teleportOtherToOtherOther1").replace("%player%", p.getName()).replace("%target1%", t1.map(Player::getName).orElse(""))));
                } else {
                    p.sendMessage(rs.getString("teleportOtherSelf").replace("%target%", t.map(Player::getName).orElse("")));
                    t.ifPresent(player -> player.sendMessage(trs.getString("teleportOtherOther").replace("%player%", p.getName())));

                }
                return true;
            }
            case 3 -> {
                Optional<Location> loc = Optional.ofNullable((isDouble(args[0]) && isDouble(args[1]) && isDouble(args[2]) ? new Location(p.getWorld(), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2])) : getRelativeLocation(p.getLocation(), args[0], args[1], args[2])));
                if (loc.isEmpty()) return true;
                p.teleport(loc.orElse(null));
                p.sendMessage(rs.getString("teleportToCoordinates").replace("%x%", "" + loc.map(Location::getX)).replace("%y%", "" + loc.map(Location::getY)).replace("%z%", "" + loc.map(Location::getY)));
                return true;
            }
            case 4 -> {
                Optional<Location> loc = Optional.of((isDouble(args[1]) && isDouble(args[2]) && isDouble(args[3]) ? new Location(p.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3])) : Optional.ofNullable(getRelativeLocation(p.getLocation(), args[1], args[2], args[3])).orElse(p.getLocation())));
                Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(args[0]));
                if (t.isEmpty()) return true;
                t.ifPresent(player -> player.teleport(loc.orElse(null)));
                ResourceBundle trs = LanguageHandler.getResourceBundle(t.map(Player::getUniqueId).orElse(null));
                p.sendMessage(rs.getString("teleportToCoordinatesOtherSelf").replace("%target%", t.map(Player::getName).orElse("")).replace("%x%", "" + loc.map(Location::getX)).replace("%y%", "" + loc.map(Location::getY)).replace("%z%", "" + loc.map(Location::getZ)));
                t.ifPresent(player -> player.sendMessage(trs.getString("teleportToCoordinatesOtherOther").replace("%player%", p.getName()).replace("%x%", "" + loc.map(Location::getX)).replace("%y%", "" + loc.map(Location::getY)).replace("%z%", "" + loc.map(Location::getZ))));
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
        Location targetLocation = p.getTargetBlock(null, 100).getLocation();
        switch (args.length) {

            case 1 -> {
                List<String> possibilities = new ArrayList<>();
                Bukkit.getOnlinePlayers().forEach(player -> possibilities.add(player.getName()));
                possibilities.add(String.valueOf(targetLocation.getX()));
                return possibilities;
            }

            case 2 -> {
                List<String> possibilities = new ArrayList<>();
                Bukkit.getOnlinePlayers().forEach(player -> possibilities.add(player.getName()));
                if (isDouble(args[0]) && Double.parseDouble(args[0]) == targetLocation.getX())
                    possibilities.add(String.valueOf(targetLocation.getY()));
                else possibilities.add(String.valueOf(targetLocation.getX()));
                return possibilities;
            }

            case 3 -> {
                if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1]))) return Collections.emptyList();
                List<String> possibilities = new ArrayList<>();
                if (isDouble(args[1]) && Double.parseDouble(args[1]) == targetLocation.getY())
                    possibilities.add(String.valueOf(targetLocation.getZ()));
                else possibilities.add(String.valueOf(targetLocation.getY()));
                return possibilities;
            }

            case 4 -> {
                if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1]))) return Collections.emptyList();
                List<String> possibilities = new ArrayList<>();
                if (isDouble(args[2]) && Double.parseDouble(args[2]) == targetLocation.getZ())
                    return Collections.emptyList();
                else possibilities.add(String.valueOf(targetLocation.getZ()));
                return possibilities;
            }

        }
        return null;
    }

    private Location getRelativeLocation(Location loc, String x, String y, String z) {
        if (!(x.startsWith("~") && y.startsWith("~") && z.startsWith("~"))) return null;
        double newX = (x.length() == 1) ? loc.getX() : loc.getX() + Double.parseDouble(x.substring(1));
        double newY = (y.length() == 1) ? loc.getY() : loc.getY() + Double.parseDouble(y.substring(1));
        double newZ = (z.length() == 1) ? loc.getZ() : loc.getZ() + Double.parseDouble(z.substring(1));
        return new Location(loc.getWorld(), newX, newY, newZ, loc.getYaw(), loc.getPitch());
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
