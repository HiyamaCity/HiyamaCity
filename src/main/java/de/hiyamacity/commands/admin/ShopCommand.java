package de.hiyamacity.commands.admin;

import de.hiyamacity.items.weapons.Katana;
import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.Shop;
import de.hiyamacity.objects.ShopType;
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

public class ShopCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
        if (!p.hasPermission("shop")) return true;
        args[0] = args[0].toLowerCase();

        if (ShopType.getAllTypes().contains(args[0])) {
            p.sendMessage(rs.getString("shopUnknownType"));
            return true;
        }

        switch (ShopType.valueOf(args[0])) {

            case DRINK:
                Shop shop = new Shop("", null, new ArrayList<>(), new ShopType[]{ShopType.DRINK}, p.getLocation());
                p.sendMessage(shop.toString());
                break;
            case FOOD:
                break;
            case AGRICULTURE:
                break;
            case SOUVENIRS:
                break;
            case ORES:
                break;
            case ARMOR:
                break;
            case PET:
                break;
            case TIMBER:
                break;
            case STONES:
                break;
            case NETHER:
                break;
            case END:
                break;
            case DECORATION:
                break;
            case WORKINGBLOCKS:
                break;
            case WEAPONS:
                break;
            default:
                break;

        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) return ShopType.getAllTypes();
        return null;
    }
}
