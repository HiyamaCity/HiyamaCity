package de.hiyamacity.items.collectables;

import de.hiyamacity.items.HiyamaCityItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class Collectable extends HiyamaCityItem {

    private boolean collected;

    public Collectable(ItemStack itemStack, int shopPrice) {
        super(itemStack, shopPrice);
    }
}
