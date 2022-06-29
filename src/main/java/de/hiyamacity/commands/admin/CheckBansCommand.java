package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.User;
import de.hiyamacity.util.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.*;

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
        Optional<User> user = User.getUser(uuid);
        Locale locale = user.map(User::getLocale).map(de.hiyamacity.objects.Locale::getJavaUtilLocale).orElse(LanguageHandler.defaultLocale);
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
        sender.sendMessage(rs.getString("checkBansHeader").replace("%target%", Optional.ofNullable(Bukkit.getPlayer(uuid)).map(Player::getName).orElseGet(() -> Optional.ofNullable(Bukkit.getOfflinePlayer(uuid).getName()).orElse(""))));
        BanManager.getBans(uuid).ifPresent(bans -> bans.forEach(ban -> sender.sendMessage(rs.getString("checkBansMessage").replace("%reason%", (ban.getBanReason() == null) ? "" : ban.getBanReason()).replace("%id%", (ban.getBanID() == null) ? "" : ban.getBanID()).replace("%banStart%", (ban.getBanStart() == 0) ? "" : dateFormat.format(ban.getBanStart())).replace("%banEnd%", (ban.getBanEnd() == 0) ? "" : dateFormat.format(ban.getBanEnd())).replace("%boolean%", String.valueOf(ban.isActive())).replace("%createdBy%", Optional.ofNullable(Bukkit.getPlayer(ban.getCreatedBy())).map(Player::getName).orElseGet(() -> Optional.ofNullable(Bukkit.getOfflinePlayer(ban.getCreatedBy()).getName()).orElse(""))))));
        sender.sendMessage(rs.getString("checkBansFooter"));

        return false;
    }
}
