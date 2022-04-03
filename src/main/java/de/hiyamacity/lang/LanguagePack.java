package de.hiyamacity.lang;

import java.util.ListResourceBundle;

public class LanguagePack extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private final Object[][] contents = {
            {"tablistHeader", " \n     §b§lHiyamaCity §r§7- §cFantasy §7& §cReallife Role-play     "},
            {"tablistFooter", "§7%current%§8/§7%max%§a Spieler online.\n "},
            // Errors
            {"playerNotFound", "§cFehler: Der Spieler %target% wurde nicht gefunden."},
            {"playerTooFarAway", "§cFehler: Der angegebene Spieler ist zu weit entfernt."},
            {"payNonNegative", "§cFehler: Der angegebene Betrag darf nicht < 0 sein."},
            {"payInsufficientAmount", "§cFehler: Du kannst nicht mehr Geld abgeben als du dabei hast."},
            {"payCantPaySelf", "§cFehler: Du kannst dir selber kein Geld zustecken."},
            {"gmInvalidGameMode", "§cFehler: Dein angegebener GameMode muss ≤ 0 ≤ 3 sein."},
            {"kissSelf", "§cFehler: Du kannst dich nicht selber küssen."},
            {"messageNotToYourself", "§cFehler: Du kannst dir selbst keine Nachricht schicken"},
            {"slapSelf", "§cFehler: Du kannst dich nicht selbst schlagen."},
            {"houseRegisterNonOpenableTargetBlock", "§cFehler: Du musst einen Block anschauen der geöffnet werden kann."},
            // Usages
            {"meUsage", "§cFehler: Benutze \"/me <Aktion>\""},
            {"kissUsage", "§cFehler: Benutze \"/kiss <Spielername>\""},
            {"messageUsage", "§cFehler: Benutze \"/m <Spielername> <Nachricht>\""},
            {"gmUsage", "§cFehler: Benutze \"/gm <0-3> [Spielername]\""},
            {"payUsage", "§cFehler: Benutze \"/pay <Spielername> <Betrag>\""},
            {"showFinancesUsage", "§cFehler: Benutze \"/showfinances <Spielername>\""},
            {"deathUsage", "§cFehler: Benutze \"/deaths <Spielernamen>\""},
            {"vanishUsage", "§cFehler: Benutze \"/vanish <Spielername>\""},
            {"slapUsage", "§cFehler: Benutze \"/slap <Spielername>\""},
            {"houseRegisterUsage", "§cFehler: Benutze \"/house register <Straße> <Hausnummer> <Stadt> <Postleitzahl> [Besitzer]\""},
            // Messages
            {"pingMessage", "§9%target% §7hat einen Ping von §9%ping%§7ms."},
            {"statsMessage", "§8==============================\n§7Spielername: §9%target%\n§7Spielzeit §9%hours%§7 std. §9%minutes%§7 min.\n§7Portemonnaie: §9%money%§7$, Bank: §9%bank%§7$.\n§8=============================="},
            {"gmPrefix", "§8[§aSpielmodus§8]§7"},
            {"gmSelfChanged", "§7Dein §aSpielmodus §7wurde zu §a%gamemode% §7geändert."},
            {"gmSelfChangedOther", "§7Du hast den §aSpielmodus §7von §a%target% §7zu §a%gamemode% §7geändert."},
            {"gmOtherChangedOther", "§a%player% §7hat deinen §aSpielmodus §7zu §a%gamemode% §7geändert."},
            {"paySend", "§7Du hast §9%target% %amount%$ §7zugesteckt."},
            {"payReceive", "§9%player% §7hat dir §9%amount%$ §7zugesteckt."},
            {"me", "§3* %player% %message%"},
            {"kiss", "§d*§5 %player%§d hat §5%target%§d einen Kuss gegeben."},
            {"dice", "§9%player% §7hat eine §9%result% §7gewürfelt."},
            {"showFinancesSelf", "§7Du hast §9%target% §7deine Finanzen gezeigt."},
            {"showFinancesOther", "§9%player%§7 gibt dir einen Einblick ins Portemonnaie von sich.\n§7Portemonnaie: §9%money%§7$."},
            {"messageSelf", "§7[§9Du §7-> §9%target%§7] §r%msg%"},
            {"messageOther", "§7[§9%player% -> §9Dir§7] §r%msg%"},
            {"deathCount", "§9%target% §7hat §9%amount% §7Tode."},
            {"vanishSelfActivate", "§7Du bist nun§a unsichtbar§7, niemand kann dich sehen..."},
            {"vanishSelfDeactivate", "§7Du bist nun wieder§c sichtbar§7, jeder kann dich sehen..."},
            {"vanishSelfActivateOther", "§7Du hast §9%target%§a unsichtbar §7gemacht..."},
            {"vanishSelfDeactivateOther", "§7Du hast §9%target% §7wieder§c sichtbar §7gemacht..."},
            {"vanishOtherActivate", "§9%player% §7hat dich§a unsichtbar §7gemacht..."},
            {"vanishOtherDeactivate", "§9%player% hat dich wieder§c sichtbar §7gemacht..."},
            {"playerSay", "§9%player%§r sagt: "},
            {"playerAsk", "§9%player%§r fragt: "},
            {"playerShout", "§9%player%§r schreit: "},
            {"playerWhisper", "§9%player%§r flüstert: "},
            {"houseRegisterSuccessful", "§aDu hast erfolgreich ein neues Haus (%address%) registriert.\nKoordinaten:\n%x%\n%y%\n%z%"}
    };
}