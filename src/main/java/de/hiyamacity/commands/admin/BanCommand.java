package de.hiyamacity.commands.admin;

import de.hiyamacity.database.MySqlPointer;
import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.Ban;
import de.hiyamacity.objects.User;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player || sender instanceof ConsoleCommandSender)) return true;
        ResourceBundle rs = (sender instanceof Player p) ? LanguageHandler.getResourceBundle(p.getUniqueId()) : LanguageHandler.getResourceBundle();
        if (!sender.hasPermission("ban")) return true;
        switch (args.length) {
            case 0 -> {
                sender.sendMessage(rs.getString("banUsage"));
                return true;
            }
            case 1 -> {
                UUID uuid = Bukkit.getPlayerUniqueId(args[0]);
                if (uuid == null) {
                    sender.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
                    return true;
                }
                User user = MySqlPointer.getUser(uuid);
                if (user == null) return true;
                Ban ban = (sender instanceof Player p) ? new Ban(p.getUniqueId()) : new Ban();
                List<Ban> bans = user.getBans();
                bans.add(ban);
                user.setBans(bans);
                MySqlPointer.updateUser(uuid, user);
                Player t = Bukkit.getPlayer(uuid);
                if (t == null) return true;
                ResourceBundle trs = LanguageHandler.getResourceBundle(uuid);
                DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.forLanguageTag(rs.getLocale().getLanguage()));
                t.kick(Component.text(trs.getString("banMessageNoReason").replace("%id%", ban.getBanID().toString()).replace("%banStart%", dateFormat.format(ban.getBanStart()))), PlayerKickEvent.Cause.BANNED);
            }
            default -> {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    stringBuilder.append(args[i]).append(" ");
                }
                String reason = stringBuilder.toString().trim();
                UUID uuid = Bukkit.getPlayerUniqueId(args[0]);
                if (uuid == null) {
                    sender.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
                    return true;
                }
                User user = MySqlPointer.getUser(uuid);
                if (user == null) return true;
                Ban ban = (sender instanceof Player p) ? new Ban(p.getUniqueId(), reason) : new Ban(reason);
                List<Ban> bans = user.getBans();
                bans.add(ban);
                user.setBans(bans);
                MySqlPointer.updateUser(uuid, user);
                Player t = Bukkit.getPlayer(uuid);
                if (t == null) return true;
                ResourceBundle trs = LanguageHandler.getResourceBundle(uuid);
                DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.forLanguageTag(rs.getLocale().getLanguage()));
                t.kick(Component.text(trs.getString("banMessageNoReason").replace("%reason%", reason).replace("%id%", ban.getBanID().toString()).replace("%banStart%", dateFormat.format(ban.getBanStart()))), PlayerKickEvent.Cause.BANNED);
            }
        }

        return false;
    }
}
