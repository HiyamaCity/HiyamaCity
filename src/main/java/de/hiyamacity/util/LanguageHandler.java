package de.hiyamacity.util;

import de.hiyamacity.dao.UserDao;
import de.hiyamacity.entity.User;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class LanguageHandler {

	private static final String baseName = "i18n";

	public static @NotNull ResourceBundle getResourceBundle(@NotNull UUID uuid) {
		Optional<User> user = UserDao.getUserByPlayerUUID(uuid);
		Locale locale = user.map(User::getLocale).orElse(Locale.GERMAN);
		if (locale == null) return getResourceBundle();
		else return ResourceBundle.getBundle(baseName, locale);
	}

	public static @NotNull ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(baseName);
	}

}
