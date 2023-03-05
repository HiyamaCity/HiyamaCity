package de.hiyamacity.util;

import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.entity.User;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class LanguageHandler {

	private static final Locale defaultLocale = Locale.GERMAN;
	private static final String baseName = "de.hiyamacity.lang.i18n";

	public static @NotNull ResourceBundle getResourceBundle(UUID uuid) {
		if(uuid == null) return getResourceBundle();
		Optional<User> user = new UserDAOImpl().getUserByPlayerUniqueId(uuid);
		Locale locale = user.map(User::getLocale).orElse(Locale.GERMAN);
		if (locale == null) return getResourceBundle();
		else return ResourceBundle.getBundle(baseName, locale);
	}

	public static @NotNull ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(baseName, defaultLocale);
	}

}