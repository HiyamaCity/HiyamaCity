package de.hiyamacity.items.consumables;

import de.hiyamacity.items.HiyamaCityItem;
import org.bukkit.Color;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class CapriSonneElfenTrank extends HiyamaCityItem {

    @SuppressWarnings("deprecation")
    public CapriSonneElfenTrank() {
        super(new Potion(PotionType.AWKWARD).toItemStack(1), 15);
        ItemStack itemStack = this.getItemStack();
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.setColor(Color.fromRGB(248, 128, 202));
        potionMeta.setDisplayName("§d§l§k###§r §d§lCapri-Sonne | Elfentrank §d§l§k###");
        itemStack.setItemMeta(potionMeta);
        itemStack.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
    }

}