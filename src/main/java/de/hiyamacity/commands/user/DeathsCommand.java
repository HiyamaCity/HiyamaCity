package de.hiyamacity.commands.user;

import de.hiyamacity.database.MySqlPointer;
import de.hiyamacity.objects.User;
import de.hiyamacity.lang.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

public class DeathsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return true;
        ResourceBundle resourceBundle = LanguageHandler.getResourceBundle(p.getUniqueId());

        if (args.length != 1) {
            p.sendMessage(resourceBundle.getString("deathUsage"));
            return true;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            p.sendMessage(resourceBundle.getString("playerNotFound").replace("%target%", args[0]));
            return true;
        }

        User user = MySqlPointer.getUser(t.getUniqueId());
        long deaths = user.getDeaths();

        p.sendMessage(resourceBundle.getString("deathCount").replace("%amount%", "" + deaths).replace("%target%", t.getName()));

        return false;
    }

}
