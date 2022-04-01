package de.hiyamacity.items;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@Getter
@Setter
public class Katana {

    private HashMap<Enchantments, Integer> enchantments = new HashMap<>();
    private final @NotNull ItemStack itemStack = new ItemStack(Material.IRON_SWORD);

    @SuppressWarnings("deprecation")
    public Katana() {
        this.enchantments.put(Enchantments.SHARPNESS, 3);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Rarity.LEGENDARY + this.getClass().getSimpleName());
        itemMeta.setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);
        itemStack.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
    }

    public ItemStack getAsItemStack() {
        return this.itemStack;
    }

}
