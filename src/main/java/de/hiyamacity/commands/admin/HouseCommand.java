package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.Address;
import de.hiyamacity.objects.DoorLocation;
import de.hiyamacity.objects.House;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class HouseCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return true;
        ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

        if (!p.hasPermission("house")) return true;
        if (args.length < 1) return true;
        switch (args[0].toLowerCase()) {
            case "register": {

                if (args.length < 5 || args.length > 6) {
                    p.sendMessage(rs.getString("houseRegisterUsage"));
                    return true;
                }

                Address address = new Address(firstLetterCapital(args[1]), Long.parseLong(args[2]), firstLetterCapital(args[3]), Long.parseLong(args[4]));
                UUID owner = null;

                if (args.length == 6 && Bukkit.getPlayerUniqueId(args[5]) != null)
                    owner = Bukkit.getPlayerUniqueId(args[5]);
                Location targetBlockLocation = p.getTargetBlock(null, 100).getLocation();
                BlockData blockData = targetBlockLocation.getBlock().getBlockData();

                if (!(blockData instanceof Openable)) {
                    p.sendMessage(rs.getString("houseRegisterNonOpenableTargetBlock"));
                    return true;
                }

                House house = new House(owner, House.generateNonOccupiedUUID(), new DoorLocation[]{new DoorLocation(targetBlockLocation.getWorld().getName(), targetBlockLocation.getX(), targetBlockLocation.getY(), targetBlockLocation.getZ())}, address);
                House.registerHouse(house);
                p.sendMessage(rs.getString("houseRegisterSuccessful").replace("%address%", address.getAsAddress()).replace("%x%", "" + targetBlockLocation.getX()).replace("%y%", "" + targetBlockLocation.getY()).replace("%z%", "" + targetBlockLocation.getZ()));

                break;
            }

            case "delete":
                break;
            default:
                return true;


        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) return new ArrayList<>(List.of("register"));
        return null;
    }

    private String firstLetterCapital(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

}
