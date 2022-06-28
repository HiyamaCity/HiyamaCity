package de.hiyamacity.commands.user;

import de.hiyamacity.objects.User;
import de.hiyamacity.util.AfkHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class AfkCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return true;
        Optional<User> user = User.getUser(p.getUniqueId());
        user.ifPresent(AfkHandler::toggleAfk);
        return false;
    }
}
