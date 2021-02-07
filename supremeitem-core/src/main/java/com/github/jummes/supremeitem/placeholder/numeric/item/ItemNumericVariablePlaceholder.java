package com.github.jummes.supremeitem.placeholder.numeric.item;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lItem Numeric Variable", description = "gui.placeholder.double.item.variable.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVjNGQyNGFmZmRkNDgxMDI2MjAzNjE1MjdkMjE1NmUxOGMyMjNiYWU1MTg5YWM0Mzk4MTU2NDNmM2NmZjlkIn19fQ")
public class ItemNumericVariablePlaceholder extends ItemNumericPlaceholder {

    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0";

    @Serializable(headTexture = NAME_HEAD, description = "gui.placeholder.double.item.variable.name")
    private String name;

    public ItemNumericVariablePlaceholder() {
        this(TARGET_DEFAULT, "var");
    }

    public ItemNumericVariablePlaceholder(boolean target, String name) {
        super(target);
        this.name = name;
    }

    public ItemNumericVariablePlaceholder(Map<String, Object> map) {
        super(map);
        this.name = (String) map.getOrDefault("name", "var");
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        ItemStack item = getItem(target, source);

        if (item == null) {
            return 0.0;
        }
        if (item.getItemMeta() == null) {
            return 0.0;
        }

        Double toReturn = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(SupremeItem.getInstance(),
                name), PersistentDataType.DOUBLE);

        return toReturn == null ? 0.0 : toReturn;
    }

    @Override
    public String getName() {
        return "Target." + name;
    }

    @Override
    public NumericPlaceholder clone() {
        return new ItemNumericVariablePlaceholder(target, name);
    }

    public ItemStack targetItem() {
        return null;
    }
}
