package de.hiyamacity.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DecimalSeperator {
    public static DecimalFormat prepareFormat(char decimalSeparator, char thousandSeparator, boolean forceFraction, byte decimalPlaces) {
        String pattern = "#,###.";
        if (decimalPlaces <= 0) {
            pattern = pattern.substring(0, pattern.length() - 1);
        } else if (forceFraction) {
            for (byte i = 0; i < decimalPlaces; i++) {
                pattern += "0";
            }
        } else {
            for (byte i = 0; i < decimalPlaces; i++) {
                pattern += "#";
            }
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
        symbols.setDecimalSeparator(decimalSeparator);
        symbols.setGroupingSeparator(thousandSeparator);
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        return decimalFormat;
    }

}