package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.LocationSource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Enumerable.Child
public class PushAction extends EntityAction {

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = HEAD)
    private double horizontalVelocity;
    @Serializable(headTexture = HEAD)
    private double verticalVelocity;

    public PushAction() {
        this(1.0, 0.5);
    }

    public static PushAction deserialize(Map<String, Object> map) {
        double horizontalVelocity = (double) map.getOrDefault("horizontalVelocity", 1.0);
        double verticalVelocity = (double) map.getOrDefault("verticalVelocity", 0.5);
        return new PushAction(horizontalVelocity, verticalVelocity);
    }

    @Override
    protected void execute(Target target, Source source) {
        Vector difference = null;
        EntityTarget entity = (EntityTarget) target;
        if (source instanceof EntitySource) {
            difference = entity.getTarget().getLocation().toVector().
                    subtract(((EntitySource) source).getSource().getLocation().toVector());
        } else if (source instanceof LocationSource) {
            difference = entity.getTarget().getLocation().toVector().
                    subtract(((LocationSource) source).getSource().toVector());
        }
        if (difference != null) {
            difference.normalize();
            if (Double.isFinite(difference.getX()) && Double.isFinite(difference.getY())
                    && Double.isFinite(difference.getZ())) {
                difference.setX(difference.getX() * horizontalVelocity);
                difference.setZ(difference.getZ() * horizontalVelocity);
                difference.setY(verticalVelocity);
                entity.getTarget().setVelocity(difference.setY(0).multiply(horizontalVelocity)
                        .add(new Vector(0, verticalVelocity, 0)));
            }
        }
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(EntityTarget.class);
    }
}
