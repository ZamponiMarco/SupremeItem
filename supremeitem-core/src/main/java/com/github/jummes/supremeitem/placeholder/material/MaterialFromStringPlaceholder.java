package com.github.jummes.supremeitem.placeholder.material;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.StringValue;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.Map;

@Enumerable.Child
@Getter
@Setter
@Enumerable.Displayable(name = "&c&lMaterial from string", description = "gui.placeholder.material.from-string.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2RkNjM5NzhlODRlMjA5MjI4M2U5Y2QwNmU5ZWY0YmMyMjhiYjlmMjIyMmUxN2VlMzgzYjFjOWQ5N2E4YTAifX19")
public class MaterialFromStringPlaceholder extends MaterialPlaceholder {

    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2RkNjM5NzhlODRlMjA5MjI4M2U5Y2QwNmU5ZWY0YmMyMjhiYjlmMjIyMmUxN2VlMzgzYjFjOWQ5N2E4YTAifX19";

    @Serializable(headTexture = NAME_HEAD, description = "gui.placeholder.material.from-string.name", additionalDescription = {"gui.additional-tooltips.recreate"})
    private StringValue name;

    public MaterialFromStringPlaceholder(boolean target, StringValue name) {
        super(target);
        this.name = name;
    }

    public MaterialFromStringPlaceholder() {
        this(TARGET_DEFAULT, new StringValue("STONE"));
    }

    public static MaterialFromStringPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        StringValue name = (StringValue) map.get("name");
        return new MaterialFromStringPlaceholder(target, name);
    }

    @Override
    public Material computePlaceholder(Target target, Source source) {
        try {
            System.out.println(name.getRealValue(target, source));
            return Material.valueOf(name.getRealValue(target, source));
        } catch (IllegalArgumentException e) {
            return Material.STONE;
        }
    }

    @Override
    public String getName() {
        return String.format("%s Type", name);
    }

    @Override
    public MaterialPlaceholder clone() {
        return null;
    }
}
