package com.github.jummes.supremeitem.placeholder;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.block.BlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.bool.BooleanPlaceholder;
import com.github.jummes.supremeitem.placeholder.material.MaterialPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import com.github.jummes.supremeitem.placeholder.string.StringPlaceholder;
import com.github.jummes.supremeitem.placeholder.vector.VectorPlaceholder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Enumerable.Parent(classArray = {NumericPlaceholder.class, BooleanPlaceholder.class, StringPlaceholder.class,
        BlockPlaceholder.class, MaterialPlaceholder.class, VectorPlaceholder.class})
public abstract class Placeholder<S> implements Model, Cloneable {

    protected static final String TARGET_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc4N2I3YWZiNWE1OTk1Mzk3NWJiYTI0NzM3NDliNjAxZDU0ZDZmOTNjZWFjN2EwMmFjNjlhYWU3ZjliOCJ9fX0==";
    protected static final boolean TARGET_DEFAULT = true;
    @Serializable(displayItem = "targetItem", description = "gui.placeholder.target")
    @Serializable.Optional(defaultValue = "TARGET_DEFAULT")
    protected boolean target;

    public Placeholder(boolean target) {
        this.target = target;
    }

    public Placeholder(Map<String, Object> map) {
        this.target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
    }

    public abstract S computePlaceholder(Target target, Source source);

    public abstract String getName();

    public ItemStack targetItem() {
        return Libs.getWrapper().skullFromValue(TARGET_HEAD);
    }

    public abstract Placeholder clone();

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(getClass().getAnnotation(Enumerable.Displayable.class).headTexture()),
                String.format("&6&l%s", getName()), Libs.getLocale().getList("gui.placeholder.description"));
    }

}
