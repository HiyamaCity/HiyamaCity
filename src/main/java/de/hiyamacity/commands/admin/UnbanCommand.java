package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.util.BanHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

public class UnbanCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player || sender instanceof ConsoleCommandSender)) return true;
        ResourceBundle rs = (sender instanceof Player p) ? LanguageHandler.getResourceBundle(p.getUniqueId()) : LanguageHandler.getResourceBundle();
        if (!sender.hasPermission("unban")) return true;

        if (args.length != 1) {
            sender.sendMessage(rs.getString("unbanUsage"));
            return true;
        }

        UUID uuid = Bukkit.getPlayerUniqueId(args[0]);

        if (uuid == null) {
            sender.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
            return true;
        }

        if (!BanHandler.isBanned(uuid)) {
            sender.sendMessage(rs.getString("playerNotBanned").replace("%target%", args[0]));
            return true;
        }

        BanHandler.unban(uuid);
        sender.sendMessage(rs.getString("unbanSuccessful").replace("%target%", (Bukkit.getPlayer(uuid) != null) ? Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName() : Objects.requireNonNull(Bukkit.getOfflinePlayer(uuid).getName())));

        return false;
    }
}
