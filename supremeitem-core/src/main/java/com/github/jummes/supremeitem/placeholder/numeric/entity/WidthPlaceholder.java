package com.github.jummes.supremeitem.placeholder.numeric.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lEntity width Placeholder", description = "gui.placeholder.double.entity.width.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ2YjEyOTNkYjcyOWQwMTBmNTM0Y2UxMzYxYmJjNTVhZTVhOGM4ZjgzYTE5NDdhZmU3YTg2NzMyZWZjMiJ9fX0=")
public class WidthPlaceholder extends AttributeNumericPlaceholder {

    public WidthPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public WidthPlaceholder(boolean target) {
        super(target);
    }

    public static WidthPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new WidthPlaceholder();
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        LivingEntity entity = getEntity(target, source);
        if (entity == null) {
            return Double.NaN;
        }

        return entity.getWidth();
    }

    @Override
    public String getName() {
        return String.format("%s Width", target ? "Target" : "Source");
    }

    @Override
    public NumericPlaceholder clone() {
        return new WidthPlaceholder(target);
    }
}
