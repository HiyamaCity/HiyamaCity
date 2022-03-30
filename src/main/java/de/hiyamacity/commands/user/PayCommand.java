package de.hiyamacity.commands.user;

import de.hiyamacity.database.ConnectionPool;
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        ResourceBundle resourceBundle = LanguageHandler.getResourceBundle(p.getUniqueId());

        //           0            1      args = 2
        // /pay <Spielername> <Betrag>

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

        User pUser = MySqlPointer.getUser(p.getUniqueId());
        User tUser = MySqlPointer.getUser(t.getUniqueId());

        long moneyUser = pUser.getPurse();
        if (moneyUser < amount) {
            p.sendMessage(resourceBundle.getString("payInsufficientAmount"));
            return true;
        }

        pUser.setPurse(pUser.getPurse() - amount);
        tUser.setPurse(tUser.getPurse() + amount);

        MySqlPointer.updateUser(p.getUniqueId(), pUser);
        MySqlPointer.updateUser(t.getUniqueId(), tUser);

        return false;
    }
}