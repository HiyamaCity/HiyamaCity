package de.hiyamacity.listener;

import de.hiyamacity.misc.Distances;
import de.hiyamacity.util.LanguageHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class ChatHandler implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onEvent(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        @NotNull Collection<Player> col = new ArrayList<>();
        p.getLocation().getNearbyEntities(Distances.CHAT_MESSAGE_FURTHEST, Distances.CHAT_MESSAGE_FURTHEST, Distances.CHAT_MESSAGE_FURTHEST).forEach(entity -> {
            if (entity instanceof Player) col.add((Player) entity);
        });
        String message = e.getMessage();
        for (Player nearby : col) {
            double distance = p.getLocation().distanceSquared(nearby.getLocation());
            ResourceBundle rs = LanguageHandler.getResourceBundle(nearby.getUniqueId());

            if (distance <= Distances.CHAT_MESSAGE_NEAREST) {
                if (!message.contains("?"))
                    nearby.sendMessage(ChatColor.WHITE + rs.getString("playerSay").replace("%player%", p.getName()));
                else nearby.sendMessage(ChatColor.WHITE + rs.getString("playerAsk").replace("%player%", p.getName()));
                continue;
            }

            if (distance <= Distances.CHAT_MESSAGE_NORMAL) {
                if (!message.contains("?"))
                    nearby.sendMessage(ChatColor.GRAY + rs.getString("playerSay").replace("%player%", p.getName()));
                else nearby.sendMessage(ChatColor.GRAY + rs.getString("playerAsk").replace("%player%", p.getName()));
                continue;
            }

            if (distance <= Distances.CHAT_MESSAGE_FURTHEST) {
                if (!message.contains("?"))
                    nearby.sendMessage(ChatColor.DARK_GRAY + rs.getString("playerSay").replace("%player%", p.getName()));
                else
                    nearby.sendMessage(ChatColor.DARK_GRAY + rs.getString("playerAsk").replace("%player%", p.getName()));
            }
        }
    }
}