package de.hiyamacity.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DecimalSeparator {
    public static DecimalFormat prepareFormat(char decimalSeparator, char thousandSeparator, boolean forceFraction, byte decimalPlaces) {
        StringBuilder pattern = new StringBuilder("#,###.");
        if (decimalPlaces <= 0) {
            pattern = new StringBuilder(pattern.substring(0, pattern.length() - 1));
        } else if (forceFraction) {
            for (byte i = 0; i < decimalPlaces; i++) {
                pattern.append("0");
            }
        } else {
            for (byte i = 0; i < decimalPlaces; i++) {
                pattern.append("#");
            }
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
        symbols.setDecimalSeparator(decimalSeparator);
        symbols.setGroupingSeparator(thousandSeparator);
        return new DecimalFormat(pattern.toString(), symbols);
    }

}