package de.hiyamacity.commands.user;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.misc.Distances;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class WhisperCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return true;
        ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
        if (args.length == 0) {
            p.sendMessage(rs.getString("whisperUsage"));
            return true;
        }
        String message = String.join(" ", args);
        Collection<? extends Player> recipients = Bukkit.getOnlinePlayers().stream().filter(player -> player.getLocation().distanceSquared(p.getLocation()) <= Distances.WHISPER).toList();
        ArrayList<String> recipientNames = new ArrayList<>();
        recipients.forEach(player -> recipientNames.add(player.getName()));
        Bukkit.getLogger().log(Level.INFO, "[CHAT] " + p.getName() + " flüstert -> " + message + " | " + recipientNames);
        recipients.forEach(player -> {
            ResourceBundle prs = LanguageHandler.getResourceBundle(player.getUniqueId());
            player.sendMessage(ChatColor.DARK_GRAY + prs.getString("playerWhisper").replace("%player%", p.getName()) + message);
        });
        return false;
    }
}
