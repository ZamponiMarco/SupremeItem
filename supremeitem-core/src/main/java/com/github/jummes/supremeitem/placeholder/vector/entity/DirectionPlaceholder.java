package com.github.jummes.supremeitem.placeholder.vector.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.math.Vector;
import com.github.jummes.supremeitem.placeholder.vector.VectorPlaceholder;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lDirection Placeholder", description = "gui.placeholder.vector.entity.direction.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg0OTI5NjgxNzFiOWZhYjk4Njg0MDU1MDNjNTc2YzQyNzIzNzQ0YTMxYmYxZTFkMGUzOWJkYTRkN2ZiMCJ9fX0=")
public class DirectionPlaceholder extends EntityVectorPlaceholder {

    public DirectionPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public DirectionPlaceholder(boolean target) {
        super(target);
    }

    public DirectionPlaceholder(Map<String, Object> map) {
        super(map);
    }


    @Override
    public Vector computePlaceholder(Target target, Source source) {
        LivingEntity entity = getEntity(target, source);
        if (entity == null) {
            return new Vector();
        }
        return new Vector(entity.getLocation().getDirection().clone());
    }

    @Override
    public String getName() {
        return String.format("%s Direction", target ? "Target" : "Source");
    }

    @Override
    public VectorPlaceholder clone() {
        return new DirectionPlaceholder(target);
    }
}
