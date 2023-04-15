package de.hiyamacity.util.player;

import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.jpa.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class RankHandler {

	private RankHandler() {
	}

	private static final Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
	private static final String ADMIN_TEAM = "00000ADMIN";
	private static final String PLAYER_TEAM = "00001PLAYER";
	private static final String AFK_TEAM = "99999AFK";

	public static void initScoreboard() {

		Team adminTeam = sb.registerNewTeam(ADMIN_TEAM);
		Team playerTeam = sb.registerNewTeam(PLAYER_TEAM);
		Team afkTeam = sb.registerNewTeam(AFK_TEAM);

		adminTeam.prefix(Component.text("§5Mentor §7• §5"));
		adminTeam.suffix(Component.text("§r"));
		adminTeam.color(NamedTextColor.DARK_PURPLE);

		playerTeam.prefix(Component.text("§7"));
		playerTeam.suffix(Component.text("§r"));
		playerTeam.color(NamedTextColor.GRAY);

		afkTeam.prefix(Component.text("§6AFK §7• §6"));
		afkTeam.suffix(Component.text("§r"));
		afkTeam.color(NamedTextColor.GOLD);
		afkTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

		updateRanks();
	}

	public static void updateRanks() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			final Optional<User> user = new UserDAOImpl().getUserByPlayerUniqueId(player.getUniqueId());
			final Optional<Team> optionalTeam = Optional.ofNullable(((Supplier<Team>) () -> {

				if (user.map(User::isAfk).orElse(false)) return sb.getTeam(AFK_TEAM);
				else if (player.hasPermission("admin")) return sb.getTeam(ADMIN_TEAM);
				else return sb.getTeam(PLAYER_TEAM);

			}).get());

			if (optionalTeam.isEmpty()) return;
			final Team team = optionalTeam.get();

			final @NotNull TextComponent name = Component.text(team.prefix() + player.getName() + team.suffix());
			player.playerListName(name);
			player.customName(name);
			player.setCustomNameVisible(true);
			player.displayName(name);
			player.setScoreboard(sb);
		});

	}

}
