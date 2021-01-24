package com.github.jummes.supremeitem.placeholder.vector.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.math.Vector;
import com.github.jummes.supremeitem.placeholder.vector.VectorPlaceholder;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Displayable
@Enumerable.Child
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

    // TODO

    @Override
    public Vector computePlaceholder(Target target, Source source) {
        LivingEntity entity = getEntity(target, source);
        if (entity == null) {
            return new Vector();
        }
        return new Vector(entity.getLocation().getDirection());
    }

    @Override
    public String getName() {
        return "Direction";
    }

    @Override
    public VectorPlaceholder clone() {
        return new DirectionPlaceholder(target);
    }
}
