package de.hiyamacity.listener;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.Ban;
import de.hiyamacity.objects.User;
import de.hiyamacity.util.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.DateFormat;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class JoinHandler implements Listener {

    @EventHandler
    public void onEvent(PlayerJoinEvent e) {
        e.setJoinMessage("");
        Player p = e.getPlayer();
        VanishHandler.updateVanish(p);
        TabListHandler.updateTab();
        RankHandler.applyPrefixes();

        List<? extends Player> playerList = Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("admin")).toList();
        playerList.forEach(player -> {
            if (e.getPlayer().getName().equals(player.getName())) return;
            player.sendMessage(LanguageHandler.getResourceBundle(player.getUniqueId()).getString("joinMessage").replace("%player%", e.getPlayer().getName()));
        });

    }

    @EventHandler
    public void onEvent(PlayerQuitEvent e) {
        e.setQuitMessage("");
        TabListHandler.updateTab();

        List<? extends Player> playerList = Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("admin")).toList();
        playerList.forEach(player -> {
            if (e.getPlayer().getName().equals(player.getName())) return;
            player.sendMessage(LanguageHandler.getResourceBundle(player.getUniqueId()).getString("quitMessage").replace("%player%", e.getPlayer().getName()));
        });
    }

    @EventHandler
    public void onEvent(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();
        if (!User.isUserExist(uuid)) new User(uuid);
        if (!BanManager.isBanned(uuid)) return;
        ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);
        Ban ban = BanManager.getLatestBan(uuid);
        if (ban == null) return;
        if (ban.getBanEnd() != 0 && ban.getBanEnd() < System.currentTimeMillis()) {
            BanManager.unban(uuid);
            return;
        }
        User user = User.getUser(uuid);
        Locale locale = (user == null || user.getLocale() == null) ? LanguageHandler.defaultLocale : user.getLocale().getJavaUtilLocale();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
        String reason = (ban.getBanReason() == null) ? "" : ban.getBanReason();
        String id = (ban.getBanID() == null) ? "" : ban.getBanID();
        String banStart = (ban.getBanStart() == 0) ? "" : dateFormat.format(ban.getBanStart());
        String banEnd = (ban.getBanEnd() == 0) ? "" : dateFormat.format(ban.getBanEnd());
        long remainingTime = ban.getBanEnd() - System.currentTimeMillis();
        Duration duration = Duration.ofMillis(remainingTime);
        long days = duration.toDays();
        duration = duration.minusDays(days);
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);
        long seconds = duration.toSeconds();
        if (ban.getBanEnd() == 0)
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, (ban.getBanReason() == null) ? rs.getString("banMessageNoReason").replace("%id%", ban.getBanID()).replace("%banStart%", dateFormat.format(ban.getBanStart())) : rs.getString("banMessage").replace("%reason%", ban.getBanReason()).replace("%id%", ban.getBanID()).replace("%banStart%", dateFormat.format(ban.getBanStart())));
        else
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, (ban.getBanReason() == null) ? rs.getString("tempBanMessageNoReason").replace("%id%", id).replace("%banStart%", banStart).replace("%banEnd%", banEnd).replace("%d%", String.valueOf(days)).replace("%h%", String.valueOf(hours)).replace("%m%", String.valueOf(minutes)).replace("%s%", String.valueOf(seconds)) : rs.getString("tempBanMessage").replace("%reason%", reason).replace("%id%", id).replace("%banStart%", banStart).replace("%banEnd%", banEnd).replace("%d%", String.valueOf(days)).replace("%h%", String.valueOf(hours)).replace("%m%", String.valueOf(minutes)).replace("%s%", String.valueOf(seconds)));
    }

    private void sendAdminMessage(String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.hasPermission("adminBroadcast")) return;
            p.sendMessage(message);
        }
    }
}