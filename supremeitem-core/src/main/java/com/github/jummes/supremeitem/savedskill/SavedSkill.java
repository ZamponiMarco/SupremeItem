package com.github.jummes.supremeitem.savedskill;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class SavedSkill implements Model {

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = HEAD, description = "gui.saved-skill.name")
    private String name;
    @Serializable(headTexture = HEAD, description = "gui.saved-skill.actions")
    private List<Action> actions;

    public SavedSkill() {
        this(RandomStringUtils.randomAlphabetic(6), Lists.newArrayList());
    }

    public static SavedSkill deserialize(Map<String, Object> map) {
        String name = (String) map.get("name");
        List<Action> actions = (List<Action>) map.get("actions");
        return new SavedSkill(name, actions);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(new ItemStack(Material.STONE), "&6&lName: &c" + name,
                Libs.getLocale().getList("gui.saved-skill.description"));
    }
}
