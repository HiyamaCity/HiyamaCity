package de.hiyamacity.lang;

import de.hiyamacity.objects.User;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LanguageHandler {
    public static final Locale defaultLocale = Locale.GERMAN;
    public static final Locale[] availableLocales = new Locale[]{defaultLocale, Locale.ENGLISH};
    public static final String baseName = "i18n";

    public static @NotNull ResourceBundle getResourceBundle(UUID uuid) {
        Optional<User> user = User.getUser(uuid);
        if (user.map(User::getLocale).isEmpty()) return getResourceBundle();
        return ResourceBundle.getBundle(baseName, user.map(User::getLocale).orElse(null).getJavaUtilLocale());
    }

    public static @NotNull ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle(baseName, defaultLocale);
    }

    public static @NotNull ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle(baseName, locale);
    }

    public static boolean isSupported(Locale locale) {
        return Arrays.stream(availableLocales).toList().contains(locale);
    }

}