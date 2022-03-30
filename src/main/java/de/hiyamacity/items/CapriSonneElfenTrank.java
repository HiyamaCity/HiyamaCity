package de.hiyamacity.items;

import org.bukkit.Color;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public class CapriSonneElfenTrank {

    private final int shopCost = 15;
    private final @NotNull ItemStack itemStack = new Potion(PotionType.AWKWARD).toItemStack(1);


    public CapriSonneElfenTrank() {
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.setColor(Color.fromRGB(248, 128, 202));
        potionMeta.setDisplayName("§d§l§k###§r §d§lCapri-Sonne | Elfentrank §d§l§k###");
        itemStack.setItemMeta(potionMeta);
        itemStack.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
    }

    public ItemStack getAsItemStack() {
        return this.itemStack;
    }

}
