package com.github.jummes.supremeitem.placeholder.numeric.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lKnockback resistance Placeholder", description = "gui.placeholder.double.entity.knock.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjUyNTU5ZjJiY2VhZDk4M2Y0YjY1NjFjMmI1ZjJiNTg4ZjBkNjExNmQ0NDY2NmNlZmYxMjAyMDc5ZDI3Y2E3NCJ9fX0=")
public class KnockbackResistancePlaceholder extends AttributeNumericPlaceholder {

    public KnockbackResistancePlaceholder() {
        this(TARGET_DEFAULT);
    }

    public KnockbackResistancePlaceholder(boolean target) {
        super(target);
    }

    public KnockbackResistancePlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        LivingEntity entity = getEntity(target, source);
        if (entity == null) {
            return Double.NaN;
        }

        return getAttributeValue(entity, Attribute.GENERIC_KNOCKBACK_RESISTANCE);
    }

    @Override
    public String getName() {
        return String.format("%s Knockback Resistance", target ? "Target" : "Source");
    }

    @Override
    public NumericPlaceholder clone() {
        return new KnockbackResistancePlaceholder(target);
    }
}
