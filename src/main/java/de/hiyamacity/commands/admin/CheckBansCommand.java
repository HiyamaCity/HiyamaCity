package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.User;
import de.hiyamacity.util.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

public class CheckBansCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player || sender instanceof ConsoleCommandSender)) return true;
        ResourceBundle rs = (sender instanceof Player p) ? LanguageHandler.getResourceBundle(p.getUniqueId()) : LanguageHandler.getResourceBundle();
        if (!sender.hasPermission("checkBans")) return true;
        if (args.length != 1) {
            sender.sendMessage(rs.getString("checkBansUsage"));
            return true;
        }
        UUID uuid = Bukkit.getPlayerUniqueId(args[0]);
        if (uuid == null) {
            sender.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
            return true;
        }
        if (!BanManager.hasBans(uuid)) {
            sender.sendMessage(rs.getString("playerWasNotBannedYet").replace("%target%", args[0]));
            return true;
        }
        User user = User.getUser(uuid);
        Locale locale = (user == null || user.getLocale() == null) ? LanguageHandler.defaultLocale : user.getLocale().getJavaUtilLocale();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
        sender.sendMessage(rs.getString("checkBansHeader").replace("%target%", (Bukkit.getPlayer(uuid) != null) ? Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName() : Objects.requireNonNull(Bukkit.getOfflinePlayer(uuid).getName())));
        BanManager.getBans(uuid).forEach(ban -> sender.sendMessage(rs.getString("checkBansMessage")
                .replace("%reason%", (ban.getBanReason() == null) ? "" : ban.getBanReason())
                .replace("%id%", (ban.getBanID() == null) ? "" : ban.getBanID())
                .replace("%banStart%", (ban.getBanStart() == 0) ? "" : dateFormat.format(ban.getBanStart()))
                .replace("%banEnd%", (ban.getBanEnd() == 0) ? "" : dateFormat.format(ban.getBanEnd()))
                .replace("%boolean%", String.valueOf(ban.isActive()))
                .replace("%createdBy%", (ban.getCreatedBy() == null) ? "" : (Bukkit.getPlayer(ban.getCreatedBy()) != null) ? Objects.requireNonNull(Bukkit.getPlayer(ban.getCreatedBy())).getName() : Objects.requireNonNull(Bukkit.getOfflinePlayer(ban.getCreatedBy()).getName()))
        ));
        sender.sendMessage(rs.getString("checkBansFooter"));

        return false;
    }
}
