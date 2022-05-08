package de.hiyamacity.lang;

import de.hiyamacity.objects.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

public class LanguageHandler {
    public static final Locale defaultLocale = Locale.GERMAN;
    public static final Locale[] availableLocales = new Locale[]{defaultLocale};

    public static @NotNull ResourceBundle getResourceBundle(UUID uuid) {
        User user = User.getUser(uuid);
        if (user == null || user.getLocale() == null) return getResourceBundle();
        return ResourceBundle.getBundle("LanguagePack", user.getLocale().getJavaUtilLocale());
    }

    public static @NotNull ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("LanguagePack", defaultLocale);
    }

    public static boolean isSupported(Locale locale) {
        return Arrays.stream(availableLocales).toList().contains(locale);
    }

}