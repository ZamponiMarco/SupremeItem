package com.github.jummes.supremeitem.placeholder.vector.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.math.Vector;
import com.github.jummes.supremeitem.placeholder.vector.VectorPlaceholder;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable
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
        return new Vector(entity.getVelocity());
    }

    @Override
    public String getName() {
        return "Velocity";
    }

    @Override
    public VectorPlaceholder clone() {
        return new VelocityPlaceholder(target);
    }
}
