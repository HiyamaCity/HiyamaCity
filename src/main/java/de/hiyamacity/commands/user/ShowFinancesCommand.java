package de.hiyamacity.commands.user;

import de.hiyamacity.misc.Distances;
import de.hiyamacity.objects.User;
import de.hiyamacity.util.DecimalSeparator;
import de.hiyamacity.lang.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class ShowFinancesCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return true;
        ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());

        if (args.length != 1) {
            p.sendMessage(rs.getString("showFinancesUsage"));
            return true;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            p.sendMessage(rs.getString("playerNotFound").replace("%target%", p.getName()));
            return true;
        }

        if (p.getLocation().distanceSquared(t.getLocation()) > Distances.SHOW_FINANCES) {
            p.sendMessage(rs.getString("playerTooFarAway"));
            return true;
        }

        Optional<User> user = User.getUser(p.getUniqueId());
        DecimalFormat decimalFormat = DecimalSeparator.prepareFormat(',', '.', false, (byte) 0);
        p.sendMessage(rs.getString("showFinancesSelf").replace("%target%", t.getName()));
        ResourceBundle trs = LanguageHandler.getResourceBundle(t.getUniqueId());
        t.sendMessage(trs.getString("showFinancesOther").replace("%player%", p.getName()).replace("%money%", decimalFormat.format(user.map(User::getPurse).orElse(Long.MIN_VALUE))));

        return false;
    }
}
