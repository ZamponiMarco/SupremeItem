package com.github.jummes.supremeitem.projectile;

import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.entity.Entity;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.List;

public class HomingProjectile extends AbstractProjectile {

    protected double turnSpeed;
    protected int projectileSpread;
    protected LivingEntity entityTarget;

    public HomingProjectile(Target target, Source source, Location location, double gravity, double initialSpeed,
                            List<Action> onEntityHitActions, List<Action> onBlockHitActions,
                            List<Action> onProjectileTickActions, Entity entity, double hitBoxSize, int maxDistance,
                            int projectileSpread, LivingEntity entityTarget, double turnSpeed) {
        super(target, source, location, gravity, initialSpeed, onEntityHitActions, onBlockHitActions,
                onProjectileTickActions, entity, hitBoxSize, maxDistance);
        this.projectileSpread = projectileSpread;
        this.entityTarget = entityTarget;
        this.turnSpeed = turnSpeed;
    }

    @Override
    protected Vector getInitialDirection() {
        Vector initialDirection = location.clone().getDirection().multiply(initialSpeed).multiply(.05);
        double spread = Math.toRadians(projectileSpread);
        initialDirection.rotateAroundX((Math.random() - 0.5) * spread);
        initialDirection.rotateAroundY((Math.random() - 0.5) * spread);
        initialDirection.rotateAroundZ((Math.random() - 0.5) * spread);
        return initialDirection;
    }

    @Override
    protected void updateLocationAndDirection() {
        location.add(initialDirection);

        double magnitude = initialDirection.length();
        initialDirection.normalize();
        Vector targetDifference = entityTarget.getEyeLocation().clone().toVector().subtract(location.clone().toVector());
        targetDifference.normalize();

        lerp(initialDirection, targetDifference, turnSpeed);
        initialDirection.multiply(magnitude);
    }

    private void lerp(Vector start, Vector end, double percent) {
        start.add(end.clone().subtract(start).multiply(percent)).normalize();
    }
}
