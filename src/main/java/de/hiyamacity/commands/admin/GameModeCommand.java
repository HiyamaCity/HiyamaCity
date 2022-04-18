package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

public class GameModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player p)) return true;


        ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());


        if (!p.hasPermission("system.gm")) /* TODO: Unknown Command zurückgeben. */ return true;
        if (args.length == 0) {
            p.sendMessage(rs.getString("gmUsage"));
            return true;
        }

        if (!isInt(args[0])) {
            p.sendMessage(rs.getString("NaN"));
            return true;
        }

        if (Integer.parseInt(args[0]) > 3 || args[0].startsWith("-")) {
            p.sendMessage(rs.getString("gmInvalidGameMode"));
            return true;
        }

        GameMode gm = switch (Integer.parseInt(args[0])) {
            case 0 -> GameMode.SURVIVAL;
            case 1 -> GameMode.CREATIVE;
            case 2 -> GameMode.ADVENTURE;
            case 3 -> GameMode.SPECTATOR;
            default -> null;
        };

        switch (args.length) {
            case 1 -> {
                if (gm == null) break;
                p.sendMessage(rs.getString("gmPrefix") + " " + rs.getString("gmSelfChanged").replace("%gameMode%", gm.name().toLowerCase().substring(0, 1).toUpperCase() + gm.name().toLowerCase().substring(1)));
                p.setGameMode(gm);
            }
            case 2 -> {
                Player t = Bukkit.getPlayer(args[1]);
                if (t == null) {
                    p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[1]));
                    return true;
                }
                assert gm != null;
                p.sendMessage(rs.getString("gmPrefix") + " " + rs.getString("gmSelfChangedOther").replace("%gameMode%", gm.name().toLowerCase().substring(0, 1).toUpperCase() + gm.name().toLowerCase().substring(1)).replace("%target%", t.getName()));
                ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());
                t.sendMessage(trs.getString("gmPrefix") + " " + trs.getString("gmOtherChangedOther").replace("%gameMode%", gm.name().toLowerCase().substring(0, 1).toUpperCase() + gm.name().toLowerCase().substring(1)).replace("%player%", p.getName()));
                t.setGameMode(gm);
            }
        }

        return false;
    }

    private boolean isInt(String str) {
        try {
            int i = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ignored) {
        }
        return false;
    }
}
