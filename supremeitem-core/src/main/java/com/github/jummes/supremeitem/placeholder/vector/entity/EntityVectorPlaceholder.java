package com.github.jummes.supremeitem.placeholder.vector.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.vector.VectorPlaceholder;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Displayable
@Enumerable.Parent(classArray = {DirectionPlaceholder.class})
public abstract class EntityVectorPlaceholder extends VectorPlaceholder {
    public EntityVectorPlaceholder(boolean target) {
        super(target);
    }

    public EntityVectorPlaceholder(Map<String, Object> map) {
        super(map);
    }

    // TODO

    protected LivingEntity getEntity(Target target, Source source) {
        if (this.target) {
            if (target instanceof EntityTarget) {
                return ((EntityTarget) target).getTarget();
            }
            return null;
        }
        return source.getCaster();
    }
}
