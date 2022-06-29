package de.hiyamacity.util;

import de.hiyamacity.main.Main;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishHandler {

    @Getter
    private static final List<Player> vanishPlayers = new ArrayList<>();

    public static void updateVanish(Player p) {
        for (Player vanish : vanishPlayers)
            if (!vanish.hasPermission("vanish.bypass"))
                p.hidePlayer(Main.getInstance(), vanish);

    }

    public static void reveal(Player p) {
        vanishPlayers.remove(p);
        TabListHandler.updateTab();
        for (Player all : Bukkit.getOnlinePlayers())
            all.showPlayer(Main.getInstance(), p);
    }

    public static void vanish(Player p) {
        vanishPlayers.add(p);
        TabListHandler.updateTab();
        for (Player all : Bukkit.getOnlinePlayers())
            if (!all.hasPermission("vanish.bypass"))
                all.hidePlayer(Main.getInstance(), p);
    }

    public static boolean isVanish(Player p) {
        return vanishPlayers.contains(p);
    }

    public static int getVanishedPlayersCount() {
        return vanishPlayers.size();
    }
}