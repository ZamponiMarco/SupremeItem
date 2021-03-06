package com.github.jummes.supremeitem.condition;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.GUINameable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.bool.TrueFalseCondition;
import com.github.jummes.supremeitem.condition.numeric.NumericCondition;
import com.github.jummes.supremeitem.condition.string.StringCondition;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Enumerable.Parent(classArray = {NumericCondition.class, TrueFalseCondition.class, StringCondition.class, AlwaysTrueCondition.class})
@GUINameable(GUIName = "getName")
public abstract class Condition implements Model, Cloneable {

    protected static final boolean NEGATE_DEFAULT = false;

    private static final String NEGATE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWZkMjQwMDAwMmFkOWZiYmJkMDA2Njk0MWViNWIxYTM4NGFiOWIwZTQ4YTE3OGVlOTZlNGQxMjlhNTIwODY1NCJ9fX0=";

    @Serializable(headTexture = NEGATE_HEAD, description = "gui.condition.negate")
    @Serializable.Optional(defaultValue = "NEGATE_DEFAULT")
    protected boolean negate;

    public Condition(boolean negate) {
        this.negate = negate;
    }

    public Condition(Map<String, Object> map) {
        this.negate = (boolean) map.getOrDefault("negate", NEGATE_DEFAULT);
    }

    public boolean checkCondition(Target target, Source source) {
        if (negate) {
            return !testCondition(target, source);
        }
        return testCondition(target, source);
    }

    public abstract boolean testCondition(Target target, Source source);

    public abstract String getName();

    @Override
    public abstract Condition clone();

    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(getClass().getAnnotation(Enumerable.Displayable.class).headTexture()),
                getName(), Libs.getLocale().getList("gui.additional-tooltips.delete"));
    }

}
