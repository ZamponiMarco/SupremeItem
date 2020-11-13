package com.github.jummes.supremeitem.projectile;

import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.entity.Entity;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public class Projectile extends AbstractProjectile {

    private int projectileSpread;

    public Projectile(Target target, Source source, Location location, double gravity, double initialSpeed,
                      List<Action> onEntityHitActions, List<Action> onBlockHitActions,
                      List<Action> onProjectileTickActions, Entity entity, double hitBoxSize, int maxDistance,
                      int projectileSpread) {
        super(target, source, location, gravity, initialSpeed, onEntityHitActions, onBlockHitActions,
                onProjectileTickActions, entity, hitBoxSize, maxDistance);
        this.projectileSpread = projectileSpread;
    }

    @Override
    protected Vector getInitialDirection() {
        Vector initialDirection = location.getDirection().multiply(initialSpeed).multiply(.05);
        double spread = Math.toRadians(projectileSpread);
        initialDirection.rotateAroundX((Math.random() - 0.5) * spread);
        initialDirection.rotateAroundY((Math.random() - 0.5) * spread);
        initialDirection.rotateAroundZ((Math.random() - 0.5) * spread);
        return initialDirection;
    }

    @Override
    protected void updateLocationAndDirection() {
        location.add(initialDirection);
    }

}
