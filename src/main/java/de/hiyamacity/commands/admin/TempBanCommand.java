package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.Ban;
import de.hiyamacity.objects.TimeUnits;
import de.hiyamacity.objects.User;
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
import java.time.Duration;
import java.util.*;

public class TempBanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player || sender instanceof ConsoleCommandSender)) return true;
        ResourceBundle rs = (sender instanceof Player p) ? LanguageHandler.getResourceBundle(p.getUniqueId()) : LanguageHandler.getResourceBundle();
        if (!sender.hasPermission("tempBan")) return true;

        if (args.length < 2) {
            sender.sendMessage(rs.getString("tempBanUsage"));
            return true;
        }

        UUID uuid = Bukkit.getPlayerUniqueId(args[0]);

        if (uuid == null) {
            sender.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
            return true;
        }

        long time;
        int duration;
        String format = ((args[1].endsWith("min") ? args[1].substring(args[1].length() - 3).toLowerCase() : args[1].substring(args[1].length() - 1).toLowerCase()));
        try {
            duration = ((args[1].endsWith("min") ? Integer.parseInt(args[1].substring(0, args[1].length() - 3)) : Integer.parseInt(args[1].substring(0, args[1].length() - 1))));
        } catch (NumberFormatException e) {
            sender.sendMessage(rs.getString("tempBanNumberFormatException"));
            return true;
        }
        TimeUnits timeUnits = TimeUnits.fromString(format);
        if (timeUnits == null) {
            sender.sendMessage(rs.getString("tempBanTimeExplanation").replace("%y%", TimeUnits.YEARS.getValue()).replace("%m%", TimeUnits.MONTHS.getValue()).replace("%w%", TimeUnits.WEEKS.getValue()).replace("%d%", TimeUnits.DAYS.getValue()).replace("%h%", TimeUnits.HOURS.getValue()).replace("%min%", TimeUnits.MINUTES.getValue()).replace("%s%", TimeUnits.SECONDS.getValue()));
            return true;
        }
        switch (timeUnits) {
            case YEARS -> time = (long) duration * 1000 * 60 * 60 * 24 * 365;
            case MONTHS -> time = (long) duration * 1000 * 60 * 60 * 24 * 30;
            case WEEKS -> time = (long) duration * 1000 * 60 * 60 * 24 * 7;
            case DAYS -> time = (long) duration * 1000 * 60 * 60 * 24;
            case HOURS -> time = (long) duration * 1000 * 60 * 60;
            case MINUTES -> time = (long) duration * 1000 * 60;
            case SECONDS -> time = duration * 1000L;
            default -> {
                sender.sendMessage(rs.getString("tempBanTimeExplanation").replace("%y%", TimeUnits.YEARS.getValue()).replace("%m%", TimeUnits.MONTHS.getValue()).replace("%w%", TimeUnits.WEEKS.getValue()).replace("%d%", TimeUnits.DAYS.getValue()).replace("%h%", TimeUnits.HOURS.getValue()).replace("%min%", TimeUnits.MINUTES.getValue()).replace("%s%", TimeUnits.SECONDS.getValue()));
                return true;
            }
        }

        long banStart = System.currentTimeMillis();
        long banEnd = System.currentTimeMillis() + time;

        StringBuilder message = new StringBuilder();

        for (int i = 2; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        if (!(message.toString().equals("") || message.toString().length() == 0)) {
            if (sender instanceof Player p) BanManager.ban(uuid, p.getUniqueId(), message.toString().trim(), banEnd);
            else BanManager.ban(uuid, message.toString().trim(), banEnd);
        } else {
            if (sender instanceof Player p) BanManager.ban(uuid, p.getUniqueId(), banEnd);
            else BanManager.ban(uuid, banEnd);
        }

        Optional<Ban> ban = BanManager.getLatestBan(uuid);
        long remainingTime = banEnd - banStart;
        Duration dur = Duration.ofMillis(remainingTime);
        long days = dur.toDays();
        dur = dur.minusDays(days);
        long hours = dur.toHours();
        dur = dur.minusHours(hours);
        long minutes = dur.toMinutes();
        dur = dur.minusMinutes(minutes);
        long seconds = dur.toSeconds();
        Optional<User> user = User.getUser(uuid);
        Locale locale = user.map(User::getLocale).map(de.hiyamacity.objects.Locale::getJavaUtilLocale).orElse(LanguageHandler.defaultLocale);
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
        sender.sendMessage((ban.map(Ban::getBanReason).orElse(null) == null) ? rs.getString("tempBanMessageNoReasonSelf").replace("%target%", (Bukkit.getPlayer(uuid) != null) ? Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName() : Objects.requireNonNull(Bukkit.getOfflinePlayer(uuid).getName())).replace("%d%", String.valueOf(days)).replace("%h%", String.valueOf(hours)).replace("%m%", String.valueOf(minutes)).replace("%s%", String.valueOf(seconds)) : rs.getString("tempBanMessageSelf").replace("%target%", (Bukkit.getPlayer(uuid) != null) ? Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName() : Objects.requireNonNull(Bukkit.getOfflinePlayer(uuid).getName())).replace("%d%", String.valueOf(days)).replace("%h%", String.valueOf(hours)).replace("%m%", String.valueOf(minutes)).replace("%s%", String.valueOf(seconds)).replace("%reason%", ban.map(Ban::getBanReason).orElse(null)));
        Player t = Bukkit.getPlayer(uuid);
        if (t == null) return true;
        t.kick(Component.text((ban.map(Ban::getBanReason).orElse(null) == null) ? rs.getString("tempBanMessageNoReason").replace("%id%", ban.map(Ban::getBanID).orElse("")).replace("%banStart%", dateFormat.format(banStart)).replace("%banEnd%", dateFormat.format(banEnd)).replace("%d%", String.valueOf(days)).replace("%h%", String.valueOf(hours)).replace("%m%", String.valueOf(minutes)).replace("%s%", String.valueOf(seconds)) : rs.getString("tempBanMessage").replace("%reason%", ban.map(Ban::getBanReason).orElse(null)).replace("%id%", ban.map(Ban::getBanID).orElse("")).replace("%banStart%", dateFormat.format(banStart)).replace("%banEnd%", dateFormat.format(banEnd)).replace("%d%", String.valueOf(days)).replace("%h%", String.valueOf(hours)).replace("%m%", String.valueOf(minutes)).replace("%s%", String.valueOf(seconds))), PlayerKickEvent.Cause.BANNED);
        return false;
    }
}
