package de.hiyamacity.items.weapons;

import de.hiyamacity.items.HiyamaCityItem;
import de.hiyamacity.items.HiyamaCityRarity;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
@Setter
public class Katana extends HiyamaCityItem {

    @SuppressWarnings("deprecation")
    public Katana() {
        super(new ItemStack(Material.IRON_SWORD), 15);
        ItemStack itemStack = this.getItemStack();
        ItemMeta itemMeta = this.getItemStack().getItemMeta();
        itemMeta.setDisplayName(HiyamaCityRarity.LEGENDARY + this.getClass().getSimpleName());
        itemMeta.setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);
        itemStack.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
    }

}