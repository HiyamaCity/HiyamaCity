package de.hiyamacity.items;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class HiyamaCityItem {

    private final ItemStack itemStack;
    private final int shopPrice;

    public HiyamaCityItem(ItemStack itemStack, int shopPrice) {
        this.itemStack = itemStack;
        this.shopPrice = shopPrice;
    }
}