package de.hiyamacity.lang;

import java.util.ListResourceBundle;

@SuppressWarnings("unused")
public class i18n_de extends ListResourceBundle {
	@Override
	protected Object[][] getContents() {
		return new Object[][] {
				{"playerCommand", "§cFehler: Dieser Befehl kann nur von Spielern ausgeführt werden..."},
				{"afkJoin", "§6Du hast den AFK-Modus betreten."},
				{"afkQuit", "§6Du hast den AFK-Modus verlassen."},
				{"welcomeMessage", "§6Willkommen auf dem Server {0}."},
				{"afkFallBackLocationInfo", "§cFehler: Deine Position konnte nicht ermittelt werden.\\nWenn du wieder anwesend bist, wirst du zum Fallback-Spot teleportiert."},
				{"playerSay", "{0} sagt: „{1}“"},
				{"playerAsk", "{0} fragt: „{1}“"},
				{"payUsage", "/pay <Spieler> <Menge>"},
				{"playerNotFound", "§cFehler: Spieler \"{0}\" wurde nicht gefunden."},
				{"databasePlayerNotFound", "§cFehler: Spieler \"{0}\" wurde nicht in der Datenbank gefunden."},
				{"cantPaySelf", "§cDu versuchst dir ernsthaft selbst Geld zu geben?...\nWer kommt denn auf so eine Idee?"},
				{"playerTooFarAway", "§cDer Spieler {0} ist zu weit entfernt...\nDie maximale Distanz für diesen Befehl beträgt: {1} Blöcke."},
				{"payNonNegative", "§cDein ernst?! Du versuchst jemandem negatives Geld zu geben? Du bist ein Dieb... Sowas macht man nicht!"},
				{"payInsufficientFunds", "§cDu versuchst mehr Geld auszugeben als du hast... Pass auf dass du nicht bei der Schufa auffällig wirst..."},
				{"paySend", "§7Du hast §9{0, number}{1} §7an §9{2} §7gegeben."},
				{"payReceive", "§9{0} §7hat dir §9{1, number}{2} §7gegeben."},
				{"currencySymbol", "€"},
				{"userFetchFailed", "§cEs ist ein Fehler beim abfragen deiner Daten aufgetreten..."},
				{"printMoney", "§7Du hast §9{0, number}{1} §7in deinem Portemonnaie."}
		};
	}
}
