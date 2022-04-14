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
        if (ban.getBanEnd() != 0 && ban.getBanEnd() < System.currentTimeMillis()) BanManager.unban(uuid);
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.forLanguageTag(rs.getLocale().getLanguage()));
        e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, (ban.getBanReason() == null) ? rs.getString("banMessageNoReason").replace("%id%", ban.getBanID().toString()).replace("%banStart%", dateFormat.format(ban.getBanStart())) : rs.getString("banMessage").replace("%reason%", ban.getBanReason()).replace("%id%", ban.getBanID().toString()).replace("%banStart%", dateFormat.format(ban.getBanStart())));
    }
}