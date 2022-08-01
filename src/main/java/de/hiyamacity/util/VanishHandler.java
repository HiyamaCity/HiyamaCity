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

	/**
	 * @param player Hides the <code>vanishedPlayers</code> for <code>player</code>.
	 */
	public static void updateVanish(Player player) {
		for (Player vanish : vanishPlayers)
			if (!vanish.hasPermission("vanish.bypass"))
				player.hidePlayer(Main.getInstance(), vanish);
	}

	/**
	 * @param player The <code>player</code> that gets <code>revealed</code> for the other players.
	 */
	public static void reveal(Player player) {
		vanishPlayers.remove(player);
		TabListHandler.updateTab();
		for (Player all : Bukkit.getOnlinePlayers())
			all.showPlayer(Main.getInstance(), player);
	}

	/**
	 * @param player The <code>player</code> that gets <code>vanished</code> for the other players.
	 */
	public static void vanish(Player player) {
		vanishPlayers.add(player);
		TabListHandler.updateTab();
		for (Player all : Bukkit.getOnlinePlayers())
			if (!all.hasPermission("vanish.bypass"))
				all.hidePlayer(Main.getInstance(), player);
	}

	/**
	 * @param player The <code>player</code> that gets checked.
	 *
	 * @return true when the given player is vanished.
	 */
	public static boolean isVanish(Player player) {
		return vanishPlayers.contains(player);
	}

	/**
	 * @return Amount of the vanished players.
	 */
	public static int getVanishedPlayersCount() {
		return vanishPlayers.size();
	}
}