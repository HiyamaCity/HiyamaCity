package de.hiyamacity.commands.admin;

import de.hiyamacity.util.LanguageHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class HouseCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
        if (!p.hasPermission("house.register")) return true;
        if (args.length <= 1) return true;
        // /house register <straße> <hausnummer> <postleitzahl> [Besitzer]
        switch (args[0].toLowerCase()) {
            case "register": {

                if (args.length != 5) {
                    p.sendMessage(rs.getString("houseRegisterUsage"));
                    return true;
                }

                String street = firstLetterCapital(args[1]);
                double houseNum = Double.parseDouble(args[2]);
                double postalCode = Double.parseDouble(args[3]);

                // TODO: Finish House registration

                break;
            }


        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        return null;
    }

    private String firstLetterCapital(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

}
