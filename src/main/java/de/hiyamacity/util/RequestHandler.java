package de.hiyamacity.util;

import de.hiyamacity.lang.LanguageHandler;
import de.hiyamacity.objects.Contract;
import de.hiyamacity.objects.Request;
import de.hiyamacity.objects.User;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class RequestHandler {


    @Getter
    private static HashMap<UUID, Request<?>> requests;

    public static void sendRequest(Request<?> request) {
        Optional<Request<?>> requestOptional = Optional.ofNullable(request);
        if (requestOptional.isEmpty() || !requests.containsKey(request.getRequester())) return;
        Request<?> requestFromList = requests.get(request.getRequester());
        if (requestFromList.getRequester().equals(request.getRequester())) {
            Optional<Player> p = Optional.ofNullable(Bukkit.getPlayer(request.getRequester()));
            ResourceBundle rs = LanguageHandler.getResourceBundle(p.map(Player::getUniqueId).orElse(null));
            p.ifPresent(player -> player.sendMessage(rs.getString("alreadyOpenRequest")));
        } else {
            switch (request.getRequestType()) {
                case CONTRACT -> {
                    Contract contract = (Contract) request;
                    requests.put(contract.getRequester(), contract);
                    Optional<Player> p = Optional.ofNullable(Bukkit.getPlayer(contract.getRequester()));
                    Optional<Player> t = Optional.ofNullable(Bukkit.getPlayer(contract.getRecipient()));
                    p.ifPresent(player -> {
                        ResourceBundle rs = LanguageHandler.getResourceBundle(player.getUniqueId());
                        player.sendMessage(rs.getString("requestSend").replace("%target%", t.map(Player::getName).orElse("")));
                    });
                    t.ifPresent(player -> {
                        ResourceBundle rs = LanguageHandler.getResourceBundle(player.getUniqueId());
                        player.sendMessage(rs.getString("requestReceive").replace("%player%", p.map(Player::getName).orElse("")));
                    });
                }
                case TELEPORT, JOIN -> {}
                default -> {
                }
            }
        }
    }

    public static void acceptLastRequest(UUID requestTarget) {
        List<Request<?>> foundRequestContainingTarget = new ArrayList<>();
        requests.forEach((uuid, request) -> {
            if (request.getRecipient().equals(requestTarget)) foundRequestContainingTarget.add(request);
        });
        Optional<? extends Request<?>> requestOptional = Optional.of(foundRequestContainingTarget.get(foundRequestContainingTarget.size() - 1));
        foundRequestContainingTarget.clear();
        requestOptional.ifPresent(request -> {
            requests.remove(request.getRequester());
            Optional<Player> requester = Optional.ofNullable(Bukkit.getPlayer(request.getRequester()));
            Optional<Player> recipient = Optional.ofNullable(Bukkit.getPlayer(request.getRecipient()));
            requester.ifPresent(player -> {
                ResourceBundle rs = LanguageHandler.getResourceBundle(player.getUniqueId());
                player.sendMessage(rs.getString("requestGotAccepted").replace("%target%", recipient.map(Player::getName).orElse("")));
            });
            recipient.ifPresent(player -> {
                ResourceBundle rs = LanguageHandler.getResourceBundle(player.getUniqueId());
                player.sendMessage(rs.getString("requestAccepted").replace("%player%", requester.map(Player::getName).orElse("")));
            });
            switch (request.getRequestType()) {
                case CONTRACT -> {
                    Contract contract = (Contract) request;
                    Optional<User> optionalRequester = User.getUser(requester.map(Player::getUniqueId).orElse(null));
                    Optional<User> optionalRecipient = User.getUser(recipient.map(Player::getUniqueId).orElse(null));
                    optionalRequester.ifPresent(user -> {
                        Optional<List<Contract>> contracts = Optional.ofNullable(user.getContracts());
                        contracts.ifPresent(contractList -> contractList.add(contract));
                        user.update();
                    });
                    optionalRecipient.ifPresent(user -> {
                        Optional<List<Contract>> contracts = Optional.ofNullable(user.getContracts());
                        contracts.ifPresent(contractList -> contractList.add(contract));
                        user.update();
                    });
                }
                default -> {
                }
            }
        });
    }
}
