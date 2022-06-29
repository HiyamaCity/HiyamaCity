package de.hiyamacity.commands.admin;

import de.hiyamacity.lang.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.ResourceBundle;

public class ClearChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player || sender instanceof ConsoleCommandSender)) return true;
        ResourceBundle rs = (sender instanceof Player p) ? LanguageHandler.getResourceBundle(p.getUniqueId()) : LanguageHandler.getResourceBundle();
        if (!sender.hasPermission("clearChat")) return true;
        switch (args.length) {
            case 0 -> {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    clearChat(player);
                    ResourceBundle trs = LanguageHandler.getResourceBundle(player.getUniqueId());
                    player.sendMessage(trs.getString("clearChatBroadcast").replace("%player%", sender.getName()));
                });
                sender.sendMessage(rs.getString("clearChatAllSuccessful"));
                return true;
            }
            case 1 -> {
                Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(args[0]));
                if (t.isEmpty()) {
                    sender.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
                    return true;
                }
                clearChat(t.orElse(null));
                sender.sendMessage(rs.getString("clearChatOtherSuccessful").replace("%target%", t.map(Player::getName).orElse("")));
                return true;
            }
            default -> {
                sender.sendMessage(rs.getString("clearChatUsage"));
                return true;
            }
        }
    }

    private void clearChat(Player p) {
        if (!p.hasPermission("clearChatBypass"))
            for (int i = 0; i < 1000; i++)
                p.sendMessage(" ");
    }
}
