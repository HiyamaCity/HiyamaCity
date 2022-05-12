package de.hiyamacity.lang;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LanguageHandlerTest {

    @Test
    public void testResourceBundleDefault() {
        assertTrue(LanguageHandler.isSupported(LanguageHandler.getResourceBundle().getLocale()));
        assertEquals("Ich bin nur eine Test-Nachricht.", LanguageHandler.getResourceBundle().getString("testMessage"));
    }

    @Test
    public void testResourceBundleDE() {
        assertTrue(LanguageHandler.isSupported(Locale.GERMAN));
        assertEquals("Ich bin nur eine Test-Nachricht.", LanguageHandler.getResourceBundle(Locale.GERMAN).getString("testMessage"));
    }

    @Test
    public void testResourceBundleEN() {
        assertTrue(LanguageHandler.isSupported(Locale.ENGLISH));
        assertEquals("I'm only a Test Message.", LanguageHandler.getResourceBundle(Locale.ENGLISH).getString("testMessage"));
    }
}