package de.hiyamacity.commands.user;

import de.hiyamacity.objects.User;
import de.hiyamacity.util.DecimalSeparator;
import de.hiyamacity.lang.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return true;
        ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

        switch (args.length) {
            case 0 -> {
                User user = User.getUser(p.getUniqueId());
                if (user == null) return true;
                long hours = user.getPlayedHours();
                long minutes = user.getPlayedMinutes();
                long money = user.getPurse();
                long bank = user.getBank();

                DecimalFormat decimalFormat = DecimalSeparator.prepareFormat(',', '.', false, (byte) 0);

                String msg = LanguageHandler.getResourceBundle(p.getUniqueId()).getString("statsMessage")
                        .replace("%target%", p.getName())
                        .replace("%hours%", decimalFormat.format(hours))
                        .replace("%minutes%", decimalFormat.format(minutes))
                        .replace("%money%", decimalFormat.format(money))
                        .replace("%bank%", decimalFormat.format(bank));
                p.sendMessage(msg);
            }
            case 1 -> {
                if (!p.hasPermission("statsOther")) return true;
                UUID uuid = Bukkit.getPlayerUniqueId(args[0]);
                if (uuid == null) {
                    p.sendMessage(rs.getString("playerNotFound").replace("%target%", args[0]));
                    return true;
                }
                User user = User.getUser(uuid);
                if (user == null) return true;
                long hours = user.getPlayedHours();
                long minutes = user.getPlayedMinutes();
                long money = user.getPurse();
                long bank = user.getBank();

                DecimalFormat decimalFormat = DecimalSeparator.prepareFormat(',', '.', false, (byte) 0);

                String msg = LanguageHandler.getResourceBundle(p.getUniqueId()).getString("statsMessage")
                        .replace("%target%", (Bukkit.getPlayer(uuid) == null) ? Objects.requireNonNull(Bukkit.getOfflinePlayer(uuid).getName()) : Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName())
                        .replace("%hours%", decimalFormat.format(hours))
                        .replace("%minutes%", decimalFormat.format(minutes))
                        .replace("%money%", decimalFormat.format(money))
                        .replace("%bank%", decimalFormat.format(bank));
                p.sendMessage(msg);
            }
            default -> {
                return true;
            }
        }
        return false;
    }
}
