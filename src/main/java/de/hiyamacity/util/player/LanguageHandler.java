package de.hiyamacity.util.player;

import de.hiyamacity.dao.UserDAOImpl;
import de.hiyamacity.jpa.User;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class LanguageHandler {

	private static final Locale DEFAULT_LOCALE = Locale.GERMAN;
	private static final String BASE_NAME = "de.hiyamacity.lang.i18n";
	private LanguageHandler() {
	}

	public static @NotNull ResourceBundle getResourceBundle(UUID uuid) {
		if (uuid == null) return getResourceBundle();
		final Optional<User> user = new UserDAOImpl().getUserByPlayerUniqueId(uuid);
		final Locale locale = user.map(User::getLocale).orElse(Locale.GERMAN);
		if (locale == null) return getResourceBundle();
		else return ResourceBundle.getBundle(BASE_NAME, locale);
	}

	public static @NotNull ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(BASE_NAME, DEFAULT_LOCALE);
	}

}
