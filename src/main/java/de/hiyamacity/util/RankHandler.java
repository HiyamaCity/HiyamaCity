package de.hiyamacity.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@SuppressWarnings("deprecation")
public class RankHandler {

    private static final Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();

    public static void initScoreboard() {

        Team playerTeam = sb.registerNewTeam("00001player");
        playerTeam.setPrefix("§7");
        playerTeam.setSuffix("§r");
        playerTeam.setColor(ChatColor.GRAY);
        applyPrefixes();

    }

    public static void applyPrefixes() {
        for (Player all : Bukkit.getOnlinePlayers()) {
            Team team = sb.getTeam("00001player");
            assert team != null;
            String name = team.getPrefix() + all.getName() + team.getSuffix();
            all.setPlayerListName(name);
            all.setCustomName(name);
            all.setCustomNameVisible(true);
            all.setDisplayName(name);
            all.setScoreboard(sb);
        }

    }
}
