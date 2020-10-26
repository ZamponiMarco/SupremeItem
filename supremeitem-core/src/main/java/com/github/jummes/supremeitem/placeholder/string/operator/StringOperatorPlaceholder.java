package com.github.jummes.supremeitem.placeholder.string.operator;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.string.StringPlaceholder;
import org.bukkit.inventory.ItemStack;

@Enumerable.Parent(classArray = {FormatStringPlaceholder.class})
@Enumerable.Displayable(name = "&c&lString Operator Placeholders", description = "gui.placeholder.string.operator.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzhlZTlmZDhjMGM2ZDI5Njc1YTNhZGZkOTRjNzIzZjZkMzA2YjJhNTk4NGU2NWRiNDY3N2JhNmFjNGY5MDIwIn19fQ")
public abstract class StringOperatorPlaceholder extends StringPlaceholder {
    public StringOperatorPlaceholder(boolean target) {
        super(target);
    }

    @Override
    public ItemStack targetItem() {
        return null;
    }
}
