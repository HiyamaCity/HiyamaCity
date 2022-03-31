package de.hiyamacity.commands.user;

import de.hiyamacity.database.MySqlPointer;
import de.hiyamacity.objects.User;
import de.hiyamacity.util.DecimalSeperator;
import de.hiyamacity.util.LanguageHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;


        Player p = (Player) sender;

        User user = MySqlPointer.getUser(p.getUniqueId());
        long hours = user.getPlayedHours();
        long minutes = user.getPlayedMinutes();
        long money = user.getPurse();
        long bank = user.getBank();

        DecimalFormat decimalFormat = DecimalSeperator.prepareFormat(',', '.', false, (byte) 0);

        String msg = LanguageHandler.getResourceBundle(p.getUniqueId()).getString("statsMessage")
                .replace("%target%", p.getName())
                .replace("%hours%", decimalFormat.format(hours))
                .replace("%minutes%", decimalFormat.format(minutes))
                .replace("%money%", decimalFormat.format(money))
                .replace("%bank%", decimalFormat.format(bank));
        p.sendMessage(msg);
        return false;
    }
}
