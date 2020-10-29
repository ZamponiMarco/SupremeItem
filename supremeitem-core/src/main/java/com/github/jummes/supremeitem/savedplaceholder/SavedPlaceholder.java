package com.github.jummes.supremeitem.savedplaceholder;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.placeholder.Placeholder;
import com.github.jummes.supremeitem.placeholder.numeric.MaxHealthPlaceholder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
public class SavedPlaceholder implements Model {

    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";
    private static final String PLACEHOLDER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZlMTk3MmYyY2ZhNGQzMGRjMmYzNGU4ZDIxNTM1OGMwYzU3NDMyYTU1ZjZjMzdhZDkxZTBkZDQ0MTkxYSJ9fX0=";
    private static int counter = 1;
    @Serializable(headTexture = NAME_HEAD, description = "gui.saved-placeholder.name")
    private String name;
    @Serializable(headTexture = PLACEHOLDER_HEAD, description = "gui.saved-placeholder.placeholder")
    private Placeholder placeholder;

    // TODO Customization

    public SavedPlaceholder() {
        this("placeholder" + counter, new MaxHealthPlaceholder());
    }

    public SavedPlaceholder(String name, Placeholder placeholder) {
        counter++;
        this.name = name;
        this.placeholder = placeholder;
    }

    public static SavedPlaceholder deserialize(Map<String, Object> map) {
        String name = (String) map.get("name");
        Placeholder placeholder = (Placeholder) map.get("placeholder");
        return new SavedPlaceholder(name, placeholder);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(new ItemStack(Material.STONE), "&6&lName: &c" + name,
                Libs.getLocale().getList("gui.saved-placeholder.description"));
    }

}
