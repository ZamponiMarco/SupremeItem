package com.github.jummes.supremeitem.placeholder.numeric.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lSpeed Placeholder", description = "gui.placeholder.double.entity.speed.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTQyMzI5YTljNDEwNDA4NDE5N2JkNjg4NjE1ODUzOTg0ZDM3ZTE3YzJkZDIzZTNlNDEyZGQ0MmQ3OGI5OGViIn19fQ==")
public class SpeedPlaceholder extends AttributeNumericPlaceholder {

    public SpeedPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public SpeedPlaceholder(boolean target) {
        super(target);
    }

    public SpeedPlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        LivingEntity entity = getEntity(target, source);
        if (entity == null) {
            return Double.NaN;
        }

        return getAttributeValue(entity, Attribute.GENERIC_MOVEMENT_SPEED);
    }

    @Override
    public String getName() {
        return String.format("%s Speed", target ? "Target" : "Source");
    }

    @Override
    public NumericPlaceholder clone() {
        return new SpeedPlaceholder(target);
    }
}
