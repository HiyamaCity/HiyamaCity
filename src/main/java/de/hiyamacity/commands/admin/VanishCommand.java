package de.hiyamacity.commands.admin;

import de.hiyamacity.util.LanguageHandler;
import de.hiyamacity.util.VanishHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

public class VanishCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
        if (!p.hasPermission("vanish")) return true;
        switch (args.length) {
            case 0:
                if (!VanishHandler.isVanish(p)) {
                    VanishHandler.vanish(p);
                    p.sendMessage(rs.getString("vanishSelfActivate"));
                } else {
                    VanishHandler.reveal(p);
                    p.sendMessage(rs.getString("vanishSelfDeactivate"));
                }
                return true;
            case 1:
                Player t = Bukkit.getPlayer(args[0]);

                if (t == null) {
                    p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
                    return true;
                }

                ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());
                if (!VanishHandler.isVanish(t)) {
                    VanishHandler.vanish(t);
                    p.sendMessage(rs.getString("vanishSelfActivateOther").replace("%target%", t.getName()));
                    t.sendMessage(trs.getString("vanishOtherActivate").replace("%player%", p.getName()));
                } else {
                    VanishHandler.reveal(t);
                    p.sendMessage(rs.getString("vanishSelfDeactivateOther").replace("%target%", t.getName()));
                    t.sendMessage(trs.getString("vanishOtherDeactivate").replace("%player%", p.getName()));
                }
                return true;
            default:
                p.sendMessage(rs.getString("vanishUsage"));
                break;
        }
        return false;
    }
}
