package com.github.jummes.supremeitem.placeholder.numeric.item;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lDurability Placeholder", description = "gui.placeholder.double.item.durability.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI4MzI5ZDdkMWZkZTYyMGViYTY2MTcwZjc4NGFjZmNjNTJjNjQyZmZmZjg0YzY4YWFjMzc1MmU3OGYxNTBiOSJ9fX0=")
public class ItemDurabilityPlaceholder extends ItemNumericPlaceholder {

    public ItemDurabilityPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public ItemDurabilityPlaceholder(boolean target) {
        super(target);
    }

    public ItemDurabilityPlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        ItemStack item = getItem(target, source);
        if (item.hasItemMeta()) {
            if (item.getItemMeta() instanceof Damageable) {
                return (double) ((Damageable) item.getItemMeta()).getDamage();
            }
        }
        return Double.NaN;
    }

    @Override
    public String getName() {
        return "Target Item Durability";
    }

    @Override
    public NumericPlaceholder clone() {
        return new ItemDurabilityPlaceholder(target);
    }

    public ItemStack targetItem() {
        return null;
    }
}
