package de.hiyamacity.listener;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.misc.Distances;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ResourceBundle;

public class ChatHandler implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onEvent(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        String message = e.getMessage();
        for (Player nearby : e.getRecipients()) {
            double distance = p.getLocation().distanceSquared(nearby.getLocation());
            ResourceBundle rs = LanguageHandler.getResourceBundle(nearby.getUniqueId());

            if (distance <= Distances.CHAT_MESSAGE_NEAREST) {
                if (!message.contains("?"))
                    nearby.sendMessage(ChatColor.WHITE + rs.getString("playerSay").replace("%player%", p.getName()) + message);
                else
                    nearby.sendMessage(ChatColor.WHITE + rs.getString("playerAsk").replace("%player%", p.getName()) + message);
                continue;
            }

            if (distance <= Distances.CHAT_MESSAGE_NORMAL) {
                if (!message.contains("?"))
                    nearby.sendMessage(ChatColor.GRAY + rs.getString("playerSay").replace("%player%", p.getName()) + message);
                else
                    nearby.sendMessage(ChatColor.GRAY + rs.getString("playerAsk").replace("%player%", p.getName()) + message);
                continue;
            }

            if (distance <= Distances.CHAT_MESSAGE_FURTHEST) {
                if (!message.contains("?"))
                    nearby.sendMessage(ChatColor.DARK_GRAY + rs.getString("playerSay").replace("%player%", p.getName()) + message);
                else
                    nearby.sendMessage(ChatColor.DARK_GRAY + rs.getString("playerAsk").replace("%player%", p.getName()) + message);
            }
        }
    }
}