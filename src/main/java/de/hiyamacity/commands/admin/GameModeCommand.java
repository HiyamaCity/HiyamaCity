package de.hiyamacity.commands.admin;

import de.hiyamacity.util.LanguageHandler;
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

        if (!(sender instanceof Player)) {
            sender.sendMessage(LanguageHandler.getResourceBundle().getString("onlyAsPlayer"));
            return true;
        }

        Player p = (Player) sender;
        ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());


        if (!p.hasPermission("system.gm")) /* TODO: Unknown Command zurückgeben. */ return true;
        if (args.length == 0) {
            p.sendMessage(rs.getString("gmUsage"));
            return true;
        }

        if (Integer.parseInt(args[0]) > 3 || args[0].startsWith("-")) {
            p.sendMessage(rs.getString("gmInvalidGameMode"));
            return true;
        }

        GameMode gm = null;
        switch (Integer.parseInt(args[0])) {
            case 0:
                gm = GameMode.SURVIVAL;
                break;
            case 1:
                gm = GameMode.CREATIVE;
                break;
            case 2:
                gm = GameMode.ADVENTURE;
                break;
            case 3:
                gm = GameMode.SPECTATOR;
                break;
        }

        switch (args.length) {
            case 1:
                if (gm == null) break;
                p.sendMessage(rs.getString("gmPrefix") + " " + rs.getString("gmSelfChanged").replace("%gamemode%", gm.name().toLowerCase().substring(0, 1).toUpperCase() + gm.name().toLowerCase().substring(1)));
                p.setGameMode(gm);
                break;
            case 2:
                Player t = Bukkit.getPlayer(args[1]);
                if (t == null) {
                    p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[1]));
                    return true;
                }

                p.sendMessage(rs.getString("gmPrefix") + " " + rs.getString("gmSelfChangedOther").replace("%gamemode%", gm.name().toLowerCase().substring(0, 1).toUpperCase() + gm.name().toLowerCase().substring(1)).replace("%target%", t.getName()));
                ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());
                t.sendMessage(trs.getString("gmPrefix") + " " + trs.getString("gmOtherChangedOther").replace("%gamemode%", gm.name().toLowerCase().substring(0, 1).toUpperCase() + gm.name().toLowerCase().substring(1)).replace("%player%", p.getName()));
                t.setGameMode(gm);
        }

        return false;
    }
}
