package de.hiyamacity.util;

import de.hiyamacity.objects.Ban;
import de.hiyamacity.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class BanManager {

    public static boolean isBanned(UUID uuid) {
        AtomicBoolean isBanned = new AtomicBoolean(false);
        User user = User.getUser(uuid);
        if (user == null) return false;
        List<Ban> bans = user.getBans();
        if (bans == null) return false;
        bans.forEach(ban -> {
            if (ban.isActive()) isBanned.set(true);
        });
        return isBanned.get();
    }

    public static boolean hasBans(UUID uuid) {
        User user = User.getUser(uuid);
        if (user == null) return false;
        return !user.getBans().isEmpty();
    }

    public static Ban getLatestBan(UUID uuid) {
        return getBans(uuid).stream().reduce((ban1, ban2) -> ban2).orElse(null);
    }

    public static List<Ban> getBans(UUID uuid) {
        return Objects.requireNonNull(User.getUser(uuid)).getBans();
    }

    public static void unban(UUID uuid) {
        User user = User.getUser(uuid);
        Objects.requireNonNull(user).getBans().forEach(ban -> {
            if (ban.isActive()) ban.setActive(false);
        });
        user.update(uuid);

    }

    public static void clearBans(UUID uuid) {
        User user = Objects.requireNonNull(User.getUser(uuid));
        user.setBans(new ArrayList<>());
        user.update(uuid);
    }

    public static void ban(UUID userToBan, UUID banCreatedBy) {
        User user = User.getUser(userToBan);
        if (user == null) return;
        Ban ban = new Ban(banCreatedBy);
        List<Ban> bans = user.getBans();
        if (bans == null) {
            bans = new ArrayList<>();
        }
        bans.add(ban);
        user.setBans(bans);
        user.update(userToBan);
    }

    public static void ban(UUID userToBan) {
        User user = User.getUser(userToBan);
        if (user == null) return;
        Ban ban = new Ban();
        List<Ban> bans = user.getBans();
        if (bans == null) {
            bans = new ArrayList<>();
        }
        bans.add(ban);
        user.setBans(bans);
        user.update(userToBan);
    }

    public static void ban(UUID userToBan, String reason) {
        User user = User.getUser(userToBan);
        if (user == null) return;
        Ban ban = new Ban();
        ban.setBanReason(reason);
        List<Ban> bans = user.getBans();
        if (bans == null) {
            bans = new ArrayList<>();
        }
        bans.add(ban);
        user.setBans(bans);
        user.update(userToBan);
    }

    public static void ban(UUID userToBan, UUID banCreatedBy, String reason) {
        User user = User.getUser(userToBan);
        if (user == null) return;
        Ban ban = new Ban(banCreatedBy);
        ban.setBanReason(reason);
        List<Ban> bans = user.getBans();
        if (bans == null) {
            bans = new ArrayList<>();
        }
        bans.add(ban);
        user.setBans(bans);
        user.update(userToBan);
    }

    public static void ban(UUID userToBan, long banEnd) {
        User user = User.getUser(userToBan);
        if (user == null) return;
        Ban ban = new Ban();
        ban.setBanEnd(banEnd);
        List<Ban> bans = user.getBans();
        if (bans == null) {
            bans = new ArrayList<>();
        }
        bans.add(ban);
        user.setBans(bans);
        user.update(userToBan);
    }

    public static void ban(UUID userToBan, String reason, long banEnd) {
        User user = User.getUser(userToBan);
        if (user == null) return;
        Ban ban = new Ban();
        ban.setBanEnd(banEnd);
        ban.setBanReason(reason);
        List<Ban> bans = user.getBans();
        if (bans == null) {
            bans = new ArrayList<>();
        }
        bans.add(ban);
        user.setBans(bans);
        user.update(userToBan);
    }

    public static void ban(UUID userToBan, UUID banCreatedBy, long banEnd) {
        User user = User.getUser(userToBan);
        if (user == null) return;
        Ban ban = new Ban(banCreatedBy);
        ban.setBanEnd(banEnd);
        List<Ban> bans = user.getBans();
        if (bans == null) {
            bans = new ArrayList<>();
        }
        bans.add(ban);
        user.setBans(bans);
        user.update(userToBan);
    }

    public static void ban(UUID userToBan, UUID banCreatedBy, String reason, long banEnd) {
        User user = User.getUser(userToBan);
        if (user == null) return;
        Ban ban = new Ban(banCreatedBy);
        ban.setBanReason(reason);
        ban.setBanEnd(banEnd);
        List<Ban> bans = user.getBans();
        if (bans == null) {
            bans = new ArrayList<>();
        }
        bans.add(ban);
        user.setBans(bans);
        user.update(userToBan);
    }
}