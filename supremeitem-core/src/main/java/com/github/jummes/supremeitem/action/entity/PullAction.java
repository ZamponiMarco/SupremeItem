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
public class PullAction extends EntityAction {

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = HEAD)
    private double force;

    public PullAction() {
        this(1.0);
    }

    public static PullAction deserialize(Map<String, Object> map) {
        double force = (double) map.getOrDefault("force", 1.0);
        return new PullAction(force);
    }

    @Override
    protected void execute(Target target, Source source) {
        Vector difference = null;
        EntityTarget entity = (EntityTarget) target;
        if (source instanceof EntitySource) {
            difference = ((EntitySource) source).getSource().getLocation().toVector().
                    subtract(entity.getTarget().getLocation().toVector());
        } else if (source instanceof LocationSource) {
            difference = ((LocationSource) source).getSource().toVector().
                    subtract(entity.getTarget().getLocation().toVector());
        }
        if (difference != null) {
            difference.normalize();
            if (Double.isFinite(difference.getX()) && Double.isFinite(difference.getY())
                    && Double.isFinite(difference.getZ())) {
                difference.multiply(force);
                entity.getTarget().setVelocity(difference);
            }
        }
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(EntityTarget.class);
    }
}
