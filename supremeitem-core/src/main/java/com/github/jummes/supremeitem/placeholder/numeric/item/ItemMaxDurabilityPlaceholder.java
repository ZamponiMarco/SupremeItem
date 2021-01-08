package com.github.jummes.supremeitem.placeholder.numeric.item;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lMax Durability Placeholder", description = "gui.placeholder.double.item.max-durability.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjFiYjJjM2JkNjVjZGQ4NGE4MDRlY2E5OGI3YTQ2NzM1ZjAxZTZhMWM5MTk5ZDY2NjE2NjNkYmRiNGY1YjQifX19")
public class ItemMaxDurabilityPlaceholder extends ItemNumericPlaceholder {

    public ItemMaxDurabilityPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public ItemMaxDurabilityPlaceholder(boolean target) {
        super(target);
    }

    public ItemMaxDurabilityPlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        ItemStack item = getItem(target, source);
        if (item != null) {
            return (double) item.getType().getMaxDurability();
        }
        return Double.NaN;
    }

    public ItemStack targetItem() {
        return null;
    }

    @Override
    public String getName() {
        return "Target Item Max Durability";
    }

    @Override
    public NumericPlaceholder clone() {
        return new ItemMaxDurabilityPlaceholder(target);
    }
}
