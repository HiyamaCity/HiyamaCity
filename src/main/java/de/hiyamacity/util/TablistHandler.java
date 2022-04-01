package de.hiyamacity.util;

import de.hiyamacity.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ResourceBundle;

public class TablistHandler {

    public static void updateTab() {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers())
                    initTab(all);
            }
        };
        runnable.runTaskLaterAsynchronously(Main.getInstance(), 1);
    }

    @SuppressWarnings("deprecation")
    private static void initTab(Player p) {
        int playerCount = Bukkit.getOnlinePlayers().size() - VanishHandler.getVanishPlayerCount();
        ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
        p.setPlayerListHeaderFooter(rs.getString("tablistHeader"), rs.getString("tablistFooter").replace("%current%", "" + playerCount).replace("%max%", "" + Bukkit.getMaxPlayers()));
    }
}
