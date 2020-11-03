package com.github.jummes.supremeitem.savedplaceholder;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.database.NamedModel;
import com.github.jummes.supremeitem.placeholder.Placeholder;
import com.github.jummes.supremeitem.placeholder.numeric.MaxHealthPlaceholder;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
public class SavedPlaceholder extends NamedModel {

    private static final String PLACEHOLDER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzcyMzcwNGE5ZDU5MTBiOWNkNTA1ZGM5OWM3NzliZjUwMzc5Y2I4NDc0NWNjNzE5ZTlmNzg0ZGQ4YyJ9fX0=";

    private static int counter = 1;

    @Serializable(headTexture = PLACEHOLDER_HEAD, description = "gui.saved-placeholder.placeholder")
    private Placeholder placeholder;
    @Serializable
    private ItemStackWrapper item;

    // TODO Drag and drop to set item

    public SavedPlaceholder() {
        this("placeholder" + counter, new MaxHealthPlaceholder(), new ItemStackWrapper());
    }

    public SavedPlaceholder(String name, Placeholder placeholder, ItemStackWrapper item) {
        super(name);
        counter++;
        this.placeholder = placeholder;
        this.item = item;
    }

    public static SavedPlaceholder deserialize(Map<String, Object> map) {
        String name = (String) map.get("name");
        Placeholder placeholder = (Placeholder) map.get("placeholder");
        ItemStackWrapper item = (ItemStackWrapper) map.getOrDefault("item", new ItemStackWrapper());
        return new SavedPlaceholder(name, placeholder, item);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(item.getWrapped(), "&6&lName: &c" + name,
                Libs.getLocale().getList("gui.saved-placeholder.description"));
    }

}
