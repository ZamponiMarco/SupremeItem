package com.github.jummes.supremeitem.placeholder.vector.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.math.Vector;
import com.github.jummes.supremeitem.placeholder.vector.VectorPlaceholder;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lVelocity Placeholder", description = "gui.placeholder.vector.entity.velocity.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTQyMzI5YTljNDEwNDA4NDE5N2JkNjg4NjE1ODUzOTg0ZDM3ZTE3YzJkZDIzZTNlNDEyZGQ0MmQ3OGI5OGViIn19fQ==")
public class VelocityPlaceholder extends EntityVectorPlaceholder {

    public VelocityPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public VelocityPlaceholder(boolean target) {
        super(target);
    }

    public VelocityPlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Vector computePlaceholder(Target target, Source source) {
        LivingEntity entity = getEntity(target, source);

        if (entity == null) {
            return new Vector();
        }
        return new Vector(entity.getVelocity().clone());
    }

    @Override
    public String getName() {
        return String.format("%s Velocity", target ? "Target" : "Source");
    }

    @Override
    public VectorPlaceholder clone() {
        return new VelocityPlaceholder(target);
    }
}
