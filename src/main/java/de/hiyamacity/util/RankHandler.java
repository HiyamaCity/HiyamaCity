package de.hiyamacity.util;

import de.hiyamacity.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Optional;

@SuppressWarnings("deprecation")
public class RankHandler {

    private static final Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
    private static final String ADMIN_TEAM = "00000ADMIN";
    private static final String PLAYER_TEAM = "00001PLAYER";
    private static final String AFK_TEAM = "99999AFK";

    public static void initScoreboard() {

        Team adminTeam = sb.registerNewTeam(ADMIN_TEAM);
        Team playerTeam = sb.registerNewTeam(PLAYER_TEAM);
        Team afkTeam = sb.registerNewTeam(AFK_TEAM);

        adminTeam.setPrefix("§7");
        adminTeam.setSuffix("§r");
        adminTeam.setColor(ChatColor.GRAY);

        playerTeam.setPrefix("§7");
        playerTeam.setSuffix("§r");
        playerTeam.setColor(ChatColor.GRAY);

        afkTeam.setPrefix("§6AFK §7• §6");
        afkTeam.setSuffix("§r");
        afkTeam.setColor(ChatColor.GOLD);
        afkTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

        applyPrefixes();
    }

    public static void applyPrefixes() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Team team;
            Optional<User> user = User.getUser(player.getUniqueId());
            if (user.map(User::isAfk).orElse(false)) team = sb.getTeam(AFK_TEAM);
            else if (player.hasPermission("admin")) team = sb.getTeam(ADMIN_TEAM);
            else team = sb.getTeam(PLAYER_TEAM);
            assert team != null;
            String name = team.getPrefix() + player.getName() + team.getSuffix();
            player.setPlayerListName(name);
            player.setCustomName(name);
            player.setCustomNameVisible(true);
            player.setDisplayName(name);
            player.setScoreboard(sb);
        });

    }
}
