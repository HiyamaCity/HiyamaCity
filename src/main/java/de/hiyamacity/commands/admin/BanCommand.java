package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.Ban;
import de.hiyamacity.util.BanManager;
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
import java.util.Locale;
import java.util.Objects;
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

                if (sender instanceof Player p) BanManager.ban(uuid, p.getUniqueId());
                else BanManager.ban(uuid);
                sender.sendMessage(rs.getString("banMessageNoReasonSelf").replace("%target%", (Bukkit.getPlayer(uuid) != null) ? Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName() : Objects.requireNonNull(Bukkit.getOfflinePlayer(uuid).getName())));
                Ban ban = BanManager.getBans(uuid).stream().reduce((ban1, ban2) -> ban2).orElse(null);
                if (ban == null) return true;
                Player t = Bukkit.getPlayer(uuid);
                if (t == null) return true;
                ResourceBundle trs = LanguageHandler.getResourceBundle(uuid);
                DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.forLanguageTag(rs.getLocale().getLanguage()));
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

                if (sender instanceof Player p) BanManager.ban(uuid, p.getUniqueId(), reason);
                else BanManager.ban(uuid, reason);
                sender.sendMessage(rs.getString("banMessageSelf").replace("%reason%", reason).replace("%target%", (Bukkit.getPlayer(uuid) != null) ? Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName() : Objects.requireNonNull(Bukkit.getOfflinePlayer(uuid).getName())));
                Ban ban = BanManager.getBans(uuid).stream().reduce((ban1, ban2) -> ban2).orElse(null);
                if (ban == null) return true;
                Player t = Bukkit.getPlayer(uuid);
                if (t == null) return true;
                ResourceBundle trs = LanguageHandler.getResourceBundle(uuid);
                DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.forLanguageTag(rs.getLocale().getLanguage()));
                t.kick(Component.text(trs.getString("banMessage").replace("%reason%", reason).replace("%id%", ban.getBanID().toString()).replace("%banStart%", dateFormat.format(ban.getBanStart()))), PlayerKickEvent.Cause.BANNED);
            }
        }

        return false;
    }
}
