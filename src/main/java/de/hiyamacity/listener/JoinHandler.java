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
import java.util.*;

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
		if (!User.isUserExist(uuid)) new User(uuid).registerUser();
		if (!BanManager.isBanned(uuid)) return;
		ResourceBundle rs = LanguageHandler.getResourceBundle(uuid);
		Optional<Ban> ban = BanManager.getLatestBan(uuid);
		ban.ifPresent(ban1 -> {
			if (ban1.getBanEnd() < System.currentTimeMillis())
				BanManager.unban(uuid);
		});
		Optional<User> user = User.getUser(uuid);
		Locale locale = user.map(User::getLocale).map(de.hiyamacity.objects.Locale::getJavaUtilLocale).orElse(LanguageHandler.getDefaultLocale());
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
		String reason = ban.map(Ban::getBanReason).orElse("");
		String id = ban.map(Ban::getBanID).orElse("");
		String banStart = dateFormat.format(ban.map(Ban::getBanStart).orElse(Long.MIN_VALUE));
		String banEnd = dateFormat.format(ban.map(Ban::getBanEnd).orElse(Long.MIN_VALUE));
		long remainingTime = ban.map(Ban::getBanEnd).orElse(Long.MIN_VALUE) - System.currentTimeMillis();
		Duration duration = Duration.ofMillis(remainingTime);
		long days = duration.toDays();
		duration = duration.minusDays(days);
		long hours = duration.toHours();
		duration = duration.minusHours(hours);
		long minutes = duration.toMinutes();
		duration = duration.minusMinutes(minutes);
		long seconds = duration.toSeconds();
		if (ban.map(Ban::getBanEnd).orElse(Long.MIN_VALUE) == 0)
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, (ban.map(Ban::getBanReason).orElse(null) == null) ? rs.getString("banMessageNoReason").replace("%id%", ban.map(Ban::getBanID).orElse(null)).replace("%banStart%", dateFormat.format(ban.map(Ban::getBanStart))) : rs.getString("banMessage").replace("%reason%", ban.map(Ban::getBanReason).orElse(null)).replace("%id%", ban.map(Ban::getBanID).orElse("")).replace("%banStart%", dateFormat.format(ban.map(Ban::getBanStart))));
		else
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, (ban.map(Ban::getBanReason).orElse(null) == null) ? rs.getString("tempBanMessageNoReason").replace("%id%", id).replace("%banStart%", banStart).replace("%banEnd%", banEnd).replace("%d%", String.valueOf(days)).replace("%h%", String.valueOf(hours)).replace("%m%", String.valueOf(minutes)).replace("%s%", String.valueOf(seconds)) : rs.getString("tempBanMessage").replace("%reason%", reason).replace("%id%", id).replace("%banStart%", banStart).replace("%banEnd%", banEnd).replace("%d%", String.valueOf(days)).replace("%h%", String.valueOf(hours)).replace("%m%", String.valueOf(minutes)).replace("%s%", String.valueOf(seconds)));
	}
}