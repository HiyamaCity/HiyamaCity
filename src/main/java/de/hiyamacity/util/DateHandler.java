package de.hiyamacity.util;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class DateHandler {

	/**
	 * @param dateString Date e.g. dd.MM.yyyy
	 * @param locale     Locale
	 *
	 * @return Returns LocalDate parsed from the given String and Locale
	 */
	public static LocalDate getLocalDate(String dateString, Locale locale) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", locale);
		return LocalDate.parse(dateString, formatter);
	}

	/**
	 * @param dateLong Date in Millis
	 * @param locale   Locale
	 *
	 * @return Returns LocalDate parsed from the given String and Locale
	 */
	public static LocalDate getLocalDate(long dateLong, Locale locale) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", locale);
		return LocalDate.parse(String.valueOf(dateLong), formatter);
	}

	/**
	 * @param date           Date to format
	 * @param resourceBundle ResourceBundle of the Player
	 *
	 * @return Returns the date localized to the Players ResourceBundle
	 */
	public static String dateToLocalizedString(LocalDate date, ResourceBundle resourceBundle) {
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.forLanguageTag(resourceBundle.getLocale().getLanguage()));
		return dateFormat.format(date);
	}
}
