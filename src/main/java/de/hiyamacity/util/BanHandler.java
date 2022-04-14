package de.hiyamacity.util;

import de.hiyamacity.database.MySqlPointer;
import de.hiyamacity.objects.Ban;
import de.hiyamacity.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class BanHandler {

    public static boolean isBanned(UUID uuid) {
        AtomicBoolean isBanned = new AtomicBoolean(false);
        Objects.requireNonNull(MySqlPointer.getUser(uuid)).getBans().forEach(ban -> {
            if (ban.isBanned()) isBanned.set(true);
        });
        return isBanned.get();
    }

    public static List<Ban> getBans(UUID uuid) {
        return Objects.requireNonNull(MySqlPointer.getUser(uuid)).getBans();
    }

    public static void unban(UUID uuid) {
        User user = Objects.requireNonNull(MySqlPointer.getUser(uuid));
        user.getBans().forEach(ban -> {
            if (ban.isBanned()) ban.setBanned(false);
        });
        MySqlPointer.updateUser(uuid, user);
    }

    public static void ban(UUID userToBan, UUID banCreatedBy) {
        User user = MySqlPointer.getUser(userToBan);
        if (user == null) return;
        Ban ban = new Ban(banCreatedBy);
        List<Ban> bans = user.getBans();
        if (bans == null) {
            bans = new ArrayList<>();
        }
        bans.add(ban);
        user.setBans(bans);
        MySqlPointer.updateUser(userToBan, user);
    }

    public static void ban(UUID userToBan) {
        User user = MySqlPointer.getUser(userToBan);
        if (user == null) return;
        Ban ban = new Ban();
        List<Ban> bans = user.getBans();
        if (bans == null) {
            bans = new ArrayList<>();
        }
        bans.add(ban);
        user.setBans(bans);
        MySqlPointer.updateUser(userToBan, user);
    }

    public static void ban(UUID userToBan, String reason) {
        User user = MySqlPointer.getUser(userToBan);
        if (user == null) return;
        Ban ban = new Ban();
        ban.setBanReason(reason);
        List<Ban> bans = user.getBans();
        if (bans == null) {
            bans = new ArrayList<>();
        }
        bans.add(ban);
        user.setBans(bans);
        MySqlPointer.updateUser(userToBan, user);
    }

    public static void ban(UUID userToBan, UUID banCreatedBy, String reason) {
        User user = MySqlPointer.getUser(userToBan);
        if (user == null) return;
        Ban ban = new Ban(banCreatedBy);
        ban.setBanReason(reason);
        List<Ban> bans = user.getBans();
        if (bans == null) {
            bans = new ArrayList<>();
        }
        bans.add(ban);
        user.setBans(bans);
        MySqlPointer.updateUser(userToBan, user);
    }
}