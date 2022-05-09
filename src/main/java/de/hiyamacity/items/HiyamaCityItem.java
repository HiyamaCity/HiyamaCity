package de.hiyamacity.items;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class HiyamaCityItem {

    private final ItemStack itemStack;

    public HiyamaCityItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}