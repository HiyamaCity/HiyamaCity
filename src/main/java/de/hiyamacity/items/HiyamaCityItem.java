package de.hiyamacity.items;

import de.hiyamacity.main.Main;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Setter
public class HiyamaCityItem extends ItemStack {

    private UUID uuid;
    private NamespacedKey namespacedKey = new NamespacedKey(Main.getInstance(), "HiyamaCityItem");

    public HiyamaCityItem(ItemStack itemStack) {
        super(itemStack);
        uuid = UUID.randomUUID();
        ItemMeta itemMeta = getItemMeta();
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, uuid.toString());
        setItemMeta(itemMeta);
    }

    public HiyamaCityItem setCustomData(String key, String value) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), key), PersistentDataType.STRING, value);
        setItemMeta(itemMeta);
        return this;
    }

    public String getCustomData(String key) {
        return getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), key), PersistentDataType.STRING);
    }
    
}