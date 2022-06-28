package de.hiyamacity.util;

import de.hiyamacity.objects.Ban;
import de.hiyamacity.objects.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class BanManager {

    public static boolean isBanned(UUID uuid) {
        AtomicBoolean isBanned = new AtomicBoolean(false);
        Optional<User> user = User.getUser(uuid);
        List<Ban> bans = user.map(User::getBans).orElse(new ArrayList<>());
        if (!bans.isEmpty()) bans.forEach(ban -> {
            if (ban.isActive()) isBanned.set(true);
        });
        return isBanned.get();
    }

    public static boolean hasBans(UUID uuid) {
        Optional<User> user = User.getUser(uuid);
        return !user.map(User::getBans).map(List::isEmpty).orElse(true);
    }

    public static Optional<Ban> getLatestBan(UUID uuid) {
        Optional<List<Ban>> bans = getBans(uuid);
        return bans.map(List::stream).flatMap(banStream -> banStream.reduce((ban1, ban2) -> ban2));
    }

    public static Optional<List<Ban>> getBans(UUID uuid) {
        Optional<User> user = User.getUser(uuid);
        return Optional.of(user.map(User::getBans).orElse(new ArrayList<>()));
    }

    public static void unban(UUID uuid) {
        Optional<List<Ban>> bans = getBans(uuid);
        bans.orElse(new ArrayList<>()).forEach(ban -> {
            if (ban.isActive()) ban.setActive(false);
        });
        Optional<User> user = User.getUser(uuid);
        user.ifPresent(User::update);
    }

    public static void clearBans(UUID uuid) {
        Optional<User> user = User.getUser(uuid);
        user.map(User::getBans).orElse(new ArrayList<>()).clear();
        user.ifPresent(User::update);
    }

    public static void ban(UUID userToBan, UUID banCreatedBy) {
        Optional<User> user = User.getUser(userToBan);
        Ban ban = new Ban(banCreatedBy);
        List<Ban> bans = user.map(User::getBans).orElse(new ArrayList<>());
        bans.add(ban);
        user.ifPresent(user1 -> {
            user1.setBans(bans);
            user1.update();
        });
    }

    public static void ban(UUID userToBan) {
        Optional<User> user = User.getUser(userToBan);
        Ban ban = new Ban();
        List<Ban> bans = user.map(User::getBans).orElse(new ArrayList<>());
        bans.add(ban);
        user.ifPresent(user1 -> {
            user1.setBans(bans);
            user1.update();
        });
    }

    public static void ban(UUID userToBan, String reason) {
        Optional<User> user = User.getUser(userToBan);
        Ban ban = new Ban();
        ban.setBanReason(reason);
        List<Ban> bans = user.map(User::getBans).orElse(new ArrayList<>());
        bans.add(ban);
        user.ifPresent(user1 -> {
            user1.setBans(bans);
            user1.update();
        });
    }

    public static void ban(UUID userToBan, UUID banCreatedBy, String reason) {
        Optional<User> user = User.getUser(userToBan);
        Ban ban = new Ban(banCreatedBy);
        ban.setBanReason(reason);
        List<Ban> bans = user.map(User::getBans).orElse(new ArrayList<>());
        bans.add(ban);
        user.ifPresent(user1 -> {
            user1.setBans(bans);
            user1.update();
        });
    }

    public static void ban(UUID userToBan, long banEnd) {
        Optional<User> user = User.getUser(userToBan);
        Ban ban = new Ban();
        ban.setBanEnd(banEnd);
        List<Ban> bans = user.map(User::getBans).orElse(new ArrayList<>());
        bans.add(ban);
        user.ifPresent(user1 -> {
            user1.setBans(bans);
            user1.update();
        });
    }

    public static void ban(UUID userToBan, String reason, long banEnd) {
        Optional<User> user = User.getUser(userToBan);
        Ban ban = new Ban();
        ban.setBanEnd(banEnd);
        ban.setBanReason(reason);
        List<Ban> bans = user.map(User::getBans).orElse(new ArrayList<>());
        bans.add(ban);
        user.ifPresent(user1 -> {
            user1.setBans(bans);
            user1.update();
        });
    }

    public static void ban(UUID userToBan, UUID banCreatedBy, long banEnd) {
        Optional<User> user = User.getUser(userToBan);
        Ban ban = new Ban(banCreatedBy);
        ban.setBanEnd(banEnd);
        List<Ban> bans = user.map(User::getBans).orElse(new ArrayList<>());
        bans.add(ban);
        user.ifPresent(user1 -> {
            user1.setBans(bans);
            user1.update();
        });
    }

    public static void ban(UUID userToBan, UUID banCreatedBy, String reason, long banEnd) {
        Optional<User> user = User.getUser(userToBan);
        Ban ban = new Ban(banCreatedBy);
        ban.setBanReason(reason);
        ban.setBanEnd(banEnd);
        List<Ban> bans = user.map(User::getBans).orElse(new ArrayList<>());
        bans.add(ban);
        user.ifPresent(user1 -> {
            user1.setBans(bans);
            user1.update();
        });
    }

}