package de.hiyamacity.lang;

import org.junit.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LanguageHandlerTest {

    @Test
    public void correctResourceBundle() {
        assertEquals("Ich bin nur eine Test-Nachricht.", LanguageHandler.getResourceBundle().getString("testMessage"));
        assertEquals("I'm only a Test Message.", LanguageHandler.getResourceBundle(Locale.ENGLISH).getString("testMessage"));
    }
}