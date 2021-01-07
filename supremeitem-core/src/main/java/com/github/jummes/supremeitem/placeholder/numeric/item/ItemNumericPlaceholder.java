package com.github.jummes.supremeitem.placeholder.numeric.item;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.ItemTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Enumerable.Parent(classArray = {ItemDurabilityPlaceholder.class, ItemMaxDurabilityPlaceholder.class})
@Enumerable.Displayable(name = "&c&lItem Placeholders", description = "gui.placeholder.double.item.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTJiMzViZGE1ZWJkZjEzNWY0ZTcxY2U0OTcyNmZiZWM1NzM5ZjBhZGVkZjAxYzUxOWUyYWVhN2Y1MTk1MWVhMiJ9fX0=")
public abstract class ItemNumericPlaceholder extends NumericPlaceholder {

    public ItemNumericPlaceholder(boolean target) {
        super(target);
    }

    public ItemNumericPlaceholder(Map<String, Object> map) {
        super(map);
    }

    public ItemStack getItem(Target target, Source source) {
        if (this.target && target instanceof ItemTarget) {
            return ((ItemTarget) target).getTarget();
        }
        return null;
    }
}
