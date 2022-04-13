package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

public class KickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return true;
        ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
        if (!p.hasPermission("kick")) return true;
        // /kick <Spieler> [Grund]
        switch (args.length) {
            case 0 -> {
                p.sendMessage(rs.getString("kickUsage"));
                return true;
            }
            case 1 -> {
                Player t = Bukkit.getPlayer(args[0]);

                if (t == null) {
                    p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
                    return true;
                }

                ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());
                t.kickPlayer(trs.getString("kickMessageNoReason"));
                p.sendMessage(rs.getString("kickMessageKickedNoReason").replace("%target%", t.getName()));

                return true;
            }
            default -> {
                Player t = Bukkit.getPlayer(args[0]);

                if (t == null) {
                    p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
                    return true;
                }

                StringBuilder reason = new StringBuilder();
                for(int i = 1; i < args.length; i++) {
                    reason.append(args[i]).append(" ");
                }

                ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());
                t.kickPlayer(trs.getString("kickMessage").replace("%reason%", reason.toString().trim()));
                p.sendMessage(rs.getString("kickMessageKicked").replace("%target%", t.getName()).replace("%reason%", reason.toString().trim()));

                return true;
            }
        }
    }
}
