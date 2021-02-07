package com.github.jummes.supremeitem.placeholder.numeric.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lHeight Placeholder", description = "gui.placeholder.double.entity.height.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTc2OWUyYzEzNGVlNWZjNmRhZWZlNDEyZTRhZjNkNTdkZjlkYmIzY2FhY2Q4ZTM2ZTU5OTk3OWVjMWFjNCJ9fX0=")
public class HeightPlaceholder extends EntityNumericPlaceholder {

    public HeightPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public HeightPlaceholder(boolean target) {
        super(target);
    }

    public HeightPlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        LivingEntity entity = getEntity(target, source);
        if (entity == null) {
            return Double.NaN;
        }

        return entity.getHeight();
    }

    @Override
    public String getName() {
        return String.format("%s Height", target ? "Target" : "Source");
    }

    @Override
    public NumericPlaceholder clone() {
        return new HeightPlaceholder(target);
    }
}
