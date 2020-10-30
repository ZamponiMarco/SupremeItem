package com.github.jummes.supremeitem.savedskill;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.database.NamedModel;
import com.github.jummes.supremeitem.util.CompressUtils;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SavedSkill extends NamedModel {

    private static final List<Action> ACTIONS_DEFAULT = Lists.newArrayList();

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    private static int counter = 1;

    @Serializable(headTexture = HEAD, description = "gui.saved-skill.actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> actions;

    // TODO Customization

    public SavedSkill() {
        this("skill" + counter, Lists.newArrayList());
    }

    public SavedSkill(String name, List<Action> actions) {
        super(name);
        counter++;
        this.actions = actions;
    }

    public static SavedSkill deserialize(Map<String, Object> map) {
        String name = (String) map.get("name");
        List<Action> actions = (List<Action>) map.getOrDefault("actions", Lists.newArrayList());
        return new SavedSkill(name, actions);
    }

    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(new ItemStack(Material.STONE), "&6&lName: &c" + name,
                Libs.getLocale().getList("gui.saved-skill.description"));
    }
}
