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
				{"welcomeMessage", "§6Herzlich willkommen, {0}!"},
				{"afkFallBackLocationInfo", "§cFehler: Deine Position konnte nicht ermittelt werden.\\nWenn du wieder anwesend bist, wirst du zum Fallback-Spot teleportiert."},
				{"playerSay", "{0} sagt: „{1}“"},
				{"playerAsk", "{0} fragt: „{1}“"},
				{"payUsage", "/pay [Spieler] [Menge]"},
				{"playerNotFound", "§cFehler: Spieler \"{0}\" wurde nicht gefunden."},
				{"databasePlayerNotFound", "§cFehler: Spieler \"{0}\" wurde nicht in der Datenbank gefunden."},
				{"cantPaySelf", "§cDu versuchst dir ernsthaft selbst Geld zu geben?...\nWer kommt denn auf so eine Idee?"},
				{"playerTooFarAway", "§cDer Spieler {0} ist zu weit entfernt...\nDie maximale Distanz für diesen Befehl beträgt: {1} Blöcke."},
				{"payNonNegative", "§cDein ernst?! Du versuchst jemandem <0€ zu geben? Sowas macht man nicht!"},
				{"bankNonNegative", "§cFehler: Deine Menge muss >0 sein."},
				{"payInsufficientFunds", "§cDu versuchst mehr Geld auszugeben als du hast... Pass auf dass du nicht bei der Schufa auffällig wirst..."},
				{"paySend", "§7Du hast §9{0, number}{1} §7an §9{2} §7gegeben."},
				{"payReceive", "§9{0} §7hat dir §9{1, number}{2} §7gegeben."},
				{"currencySymbol", "€"},
				{"userFetchFailed", "§cEs ist ein Fehler beim abfragen der Daten aufgetreten..."},
				{"printMoney", "§7Du hast §9{0, number}{1} §7in deinem Portemonnaie."},
				{"atmAdminPlainUsage", "/atm [create | delete | modify | info]"},
				{"atmAdminModifyUsage", "/atm modify [current | maximum] [new value]"},
				{"atmAdminCreate", "§7ATM-§9{0}§a erfolgreich§7 erstellt.\n§9X: {1}, Y: {2}, Z: {3}"},
				{"atmAdminDelete", "§7ATM-§9{0}§a erfolgreich§7 gelöscht.\n§9X: {1}, Y: {2}, Z: {3}"}, 
				{"atmNotFound", "§cFehler: Es wurde kein ATM gefunden."},
				{"atmAdminModify", "§7ATM-§9{0}§a erfolgreich§7 modifiziert.\n§7Parameter: §9\"{1}\"§7 von §9\"{2}\"§7 auf §9\"{3}\"§7 gesetzt."},
				{"bankWithdrawDepositUsage", "/bank [abbuchen | einzahlen] [Menge]"},
				{"bankUsage", "/bank [abbuchen | einzahlen | info | überweisen]"},
				{"bankWithdrawNotEnoughMoneyInATM", "§cFehler: Du kannst nicht mehr Geld abbuchen als in dem ATM liegt."},
				{"bankWithdraw", "§7Du hast §9{0}{1}§7 erfolgreich§c abgebucht."},
				{"bankDeposit", "§7Du hast §9{0}{1}§7 erfolgreich§a eingezahlt."},
				{"inputNaN", "§cFehler: Die von dir angegebene Zahl ist ungültig. Bitte überprüfe deine Eingabe."},
				{"playtimeUsage", "/playtime [Spieler]"},
				{"playtimeMessage", "§6Spielzeit von {0}: §7{1, choice, 0#{1} Stunden|1#{1} Stunde|1<{1, number, integer} Stunden}, {2, choice, 0#{2} Minuten|1#{2} Minute|1<{2, number, integer} Minuten}"},
				{"bankInfoUsage", "/bank info"},
				{"bankInfo", "§6Informationen zum Bankkonto von {0}\n  §7• Kontostand: §9{1}{2}\n  §7• Verbleibendes Geld im Bankautomat: §9{3}{4}"},
				{"bankWithdrawNotEnoughMoneyOnBank", "§cFehler: Du kannst nicht mehr von deinem Konto abbuchen als drauf ist."},
				{"tabListHeader", "\n     §b§lHiyamaCity §r§7- §cFantasy §7& §cReal life Role-play     "},
				{"tabListFooter", "§7{0}§8/§7{1}§a Spieler online.\n "},
				{"atmLookAtSign", "§cFehler: ATM konnte nicht erstellt werden da der Block den du anschaust kein Schild ist."},
				{"bankTransferUsage", "/bank überweisen [Spieler] [Menge]"},
				{"bankTransferSend", "§7Du hast §9{0}{1}§7 an§9 {2}§7 überwiesen."},
				{"bankTransferReceive", "§9{0}§7 hat dir §9{1}{2}§7 überweisen."},
				{"bankTransferSelf", "§cFehler: Du kannst dir selbst kein Geld überweisen."},
				{"houseUsage", "/house [create | delete | modify | info]"},
				{"houseCreateUsage", "/house create"},
				{"houseDeleteUsage", "/house delete"},
				{"houseCreateSuccessful", "§7Haus-§9{0}§7, mit Hausschild-Position: §7X: §9{1} §7Y: §9{2} §7Z: §9{3}§7,§a erfolgreich§7 erstellt."},
				{"houseNotFound", "§cFehler: Es wurde kein Haus gefunden..."},
				{"houseDeleteUnsuccessful", "§cFehler: Beim löschen des Hauses ist ein fehler aufgetreten."},
				{"houseDeleteSuccessful", "§7Haus-§9{0}§7, mit Hausschild-Position: §7X: §9{1} §7Y: §9{2} §7Z: §9{3}§7,§a erfolgreich§7 gelöscht."},
				{"houseLookAtSign", "§cFehler: Haus konnte nicht erstellt werden... Kein gültiges Wandschild gefunden."},
				{"houseSignAlreadyRegistered", "§cFehler: Bei diesem Hausschild ist bereits ein Haus registriert."}
		};
	}
}
