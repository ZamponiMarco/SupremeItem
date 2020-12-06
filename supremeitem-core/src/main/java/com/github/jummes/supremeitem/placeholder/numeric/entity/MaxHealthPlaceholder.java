package com.github.jummes.supremeitem.placeholder.numeric.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lMax Health Placeholder", description = "gui.placeholder.double.entity.max-health.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzc3OTFjY2VjMTZmYjY4ZjNjOTJlMGIwMjY0Zjk2ODBlMTI0YzM4NTlkNjY0MDM1MjRiYTViOTU3NmM5ODE4In19fQ==")
public class MaxHealthPlaceholder extends AttributeNumericPlaceholder {

    public MaxHealthPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public MaxHealthPlaceholder(boolean target) {
        super(target);
    }

    public static MaxHealthPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new MaxHealthPlaceholder(target);
    }

    @Override
    public NumericPlaceholder clone() {
        return new MaxHealthPlaceholder(target);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        LivingEntity entity = getEntity(target, source);
        if (entity == null) {
            return Double.NaN;
        }

        return getAttributeValue(entity, Attribute.GENERIC_MAX_HEALTH);
    }

    @Override
    public String getName() {
        return String.format("%s Max Health", target ? "Target" : "Source");
    }
}
