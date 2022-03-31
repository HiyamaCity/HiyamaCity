package de.hiyamacity.commands.user;

import de.hiyamacity.database.MySqlPointer;
import de.hiyamacity.misc.Distances;
import de.hiyamacity.objects.User;
import de.hiyamacity.util.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

public class PayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        ResourceBundle resourceBundle = LanguageHandler.getResourceBundle(p.getUniqueId());

        if (args.length != 2) {
            p.sendMessage(resourceBundle.getString("payUsage"));
            return true;
        }
        Player t = Bukkit.getPlayer(args[0]);

        if (t == null) {
            p.sendMessage(resourceBundle.getString("playerNotFound"));
            return true;
        }

        if (p.getLocation().distanceSquared(t.getLocation()) >= Distances.CHAT_MESSAGE_NEAREST) {
            p.sendMessage(resourceBundle.getString("playerTooFarAway"));
            return true;
        }

        int amount = Integer.parseInt(args[1]);

        if (amount < 0) {
            p.sendMessage(resourceBundle.getString("payNonNegative"));
            return true;
        }

        User pUser = MySqlPointer.getUser(p.getUniqueId());
        User tUser = MySqlPointer.getUser(t.getUniqueId());

        long moneyUser = pUser.getPurse();
        if (moneyUser < amount) {
            p.sendMessage(resourceBundle.getString("payInsufficientAmount"));
            return true;
        }

        pUser.setPurse(pUser.getPurse() - amount);
        p.sendMessage(resourceBundle.getString("payOutboundMessage").replace("%target%", t.getName()).replace("%amount%", "" + amount));
        p.sendMessage("§c-" + amount + "$");
        tUser.setPurse(tUser.getPurse() + amount);
        t.sendMessage(resourceBundle.getString("payInboundMessage").replace("%player%", p.getName()).replace("%amount%", "" + amount));
        t.sendMessage("§a+" + amount + "$");

        MySqlPointer.updateUser(p.getUniqueId(), pUser);
        MySqlPointer.updateUser(t.getUniqueId(), tUser);

        return false;
    }
}