package com.github.jummes.supremeitem.placeholder.material;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.StringValue;
import org.bukkit.Material;

import java.util.Map;

@Enumerable.Child
public class MaterialFromStringPlaceholder extends MaterialPlaceholder {

    private static final String BLOCK_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ZmOTgxN2Q3NjdkMmVkZTcxODFhMDU3YWEyNmYwOGY3ZWNmNDY1MWRlYzk3ZGU1YjU0ZWVkZTFkZDJiNDJjNyJ9fX0";

    @Serializable(headTexture = BLOCK_HEAD, description = "gui.placeholder.material.block.placeholder", additionalDescription = {"gui.additional-tooltips.recreate"})
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
            return Material.valueOf(name.getRealValue(target, source));
        } catch (IllegalArgumentException e) {
            return Material.STONE;
        }
    }

    @Override
    public String getName() {
        return "sas";
    }

    @Override
    public MaterialPlaceholder clone() {
        return null;
    }
}
