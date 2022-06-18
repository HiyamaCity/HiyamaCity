package de.hiyamacity.util;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.Contract;
import de.hiyamacity.objects.Request;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.UUID;

public class RequestHandler {


    @Getter
    private static HashMap<UUID, ? extends Request> requests;


    public static <T extends Request> void sendRequest(T request) {
        if (request == null) return;
        if (request.getRequestedAt() == 0 || request.getRequester() == null || request.getRequestType() == null || request.getRecipient() == null)
            return;
        if (requests.containsKey(request.getRequester())) {
            Request requestFromList = requests.get(request.getRequester());
            if (requestFromList.getRequester().equals(request.getRequester())) {
                Player p = Bukkit.getPlayer(request.getRequester());
                if (p == null) return;
                ResourceBundle rs = LanguageHandler.getResourceBundle(p.getUniqueId());
                p.sendMessage(rs.getString("alreadyOpenRequest"));
            }
        } else {

            switch (request.getRequestType()) {
                case CONTRACT -> {
                    Contract contract = (Contract) request;
                    // TODO: Finish Contracts
                }
            }
        }
    }
}
