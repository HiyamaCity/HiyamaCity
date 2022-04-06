package de.hiyamacity.items;

import lombok.Getter;
import org.bukkit.ChatColor;

@Getter
public enum HiyamaCityRarity {

    MYTHIC(ChatColor.LIGHT_PURPLE),
    LEGENDARY(ChatColor.GOLD),
    EPIC(ChatColor.DARK_PURPLE),
    RARE(ChatColor.BLUE),
    UNCOMMON(ChatColor.GREEN),
    COMMON(ChatColor.WHITE);

    private final ChatColor chatColor;

    HiyamaCityRarity(ChatColor color) {
        this.chatColor = color;
    }

    @Override
    public String toString() {
        return chatColor.toString();
    }

}