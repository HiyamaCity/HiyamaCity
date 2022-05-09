package de.hiyamacity.items.collectables;

import com.google.gson.GsonBuilder;
import de.hiyamacity.items.HiyamaCityItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class Collectable extends HiyamaCityItem {

    private boolean collected;

    public Collectable(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public String toString() {
        return new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }
}
