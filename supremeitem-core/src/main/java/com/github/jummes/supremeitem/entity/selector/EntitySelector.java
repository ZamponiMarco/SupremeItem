package com.github.jummes.supremeitem.entity.selector;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.google.common.collect.Lists;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Predicate;

@Enumerable.Parent(classArray = {SourceSelector.class, FieldOfViewSelector.class, ConditionSelector.class})
public abstract class EntitySelector implements Model, Cloneable {

    protected static final boolean DENY_DEFAULT = false;

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = HEAD, description = "gui.entity.selector.deny")
    @Serializable.Optional(defaultValue = "DENY_DEFAULT")
    protected boolean deny;

    public EntitySelector() {
        this(DENY_DEFAULT);
    }

    public EntitySelector(Map<String, Object> map) {
        this.deny = (boolean) map.getOrDefault("deny", DENY_DEFAULT);
    }

    public EntitySelector(boolean deny) {
        this.deny = deny;
    }

    public Predicate<LivingEntity> getFilter(Source source, Target target) {
        return this.deny ? getAbstractFilter(source, target).negate() : getAbstractFilter(source, target);
    }

    protected abstract Predicate<LivingEntity> getAbstractFilter(Source source, Target target);

    public abstract EntitySelector clone();

    protected abstract String getAbstractName();

    private String getName() {
        return String.format("%s " + getAbstractName(), deny ? "&c&lDeny" : "&a&lAllow");
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(getClass().getAnnotation(Enumerable.Displayable.class).headTexture()),
                getName(), Lists.newArrayList());
    }

}
