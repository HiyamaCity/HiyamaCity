package de.hiyamacity.commands.user;

import de.hiyamacity.util.LanguageHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

public class MessageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender Sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(Sender instanceof Player)) {
            Player p = (Player) Sender;
            ResourceBundle resourceBundle = LanguageHandler.getResourceBundle(p.getUniqueId());
            return true;
        }
        // Ich weiß nicht genau wie ich das machen muss, wenn er quasi akzeptieren soll das es auch mehr als 2 Stellen geben kann
        if ( args.length !=2){
            p.sendmessage(resourceBundle.getString("messageUsage"));
            return true;
        }

        return false;
    }
}
