package de.hiyamacity.listener;

import de.hiyamacity.database.MySqlPointer;
import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.Ban;
import de.hiyamacity.objects.User;
import de.hiyamacity.util.BanManager;
import de.hiyamacity.util.RankHandler;
import de.hiyamacity.util.TablistHandler;
import de.hiyamacity.util.VanishHandler;
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

@SuppressWarnings("deprecation")
public class JoinHandler implements Listener {

    @EventHandler
    public void onEvent(PlayerJoinEvent e) {
        e.setJoinMessage("");
        Player p = e.getPlayer();
        VanishHandler.updateVanish(p);
        TablistHandler.updateTab();
        RankHandler.applyPrefixes();
    }

    @EventHandler
    public void onEvent(PlayerQuitEvent e) {
        e.setQuitMessage("");
    }

    @EventHandler
    public void onEvent(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();
        if (!MySqlPointer.isUserExist(uuid)) MySqlPointer.registerUser(uuid, new User(uuid));

        if (!BanManager.isBanned(uuid)) return;
        ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);
        List<Ban> bans = BanManager.getBans(uuid);
        Ban ban = bans.stream().reduce((ban1, ban2) -> ban2).orElse(null);
        if (ban == null) return;
        if (ban.getBanEnd() != 0 && ban.getBanEnd() < System.currentTimeMillis()) {
            BanManager.unban(uuid);
            return;
        }
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.forLanguageTag(rs.getLocale().getLanguage()));
        String reason = (ban.getBanReason() == null) ? "" : ban.getBanReason();
        String id = (ban.getBanID() == null) ? "" : ban.getBanID().toString();
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
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, (ban.getBanReason() == null) ? rs.getString("banMessageNoReason").replace("%id%", ban.getBanID().toString()).replace("%banStart%", dateFormat.format(ban.getBanStart())) : rs.getString("banMessage").replace("%reason%", ban.getBanReason()).replace("%id%", ban.getBanID().toString()).replace("%banStart%", dateFormat.format(ban.getBanStart())));
        else
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, (ban.getBanReason() == null) ? rs.getString("tempBanMessageNoReason").replace("%id%", id).replace("%banStart%", banStart).replace("%banEnd%", banEnd).replace("%d%", String.valueOf(days)).replace("%h%", String.valueOf(hours)).replace("%m%", String.valueOf(minutes)).replace("%s%", String.valueOf(seconds)) : rs.getString("tempBanMessage").replace("%reason%", reason).replace("%id%", id).replace("%banStart%", banStart).replace("%banEnd%", banEnd).replace("%d%", String.valueOf(days)).replace("%h%", String.valueOf(hours)).replace("%m%", String.valueOf(minutes)).replace("%s%", String.valueOf(seconds)));
    }
}