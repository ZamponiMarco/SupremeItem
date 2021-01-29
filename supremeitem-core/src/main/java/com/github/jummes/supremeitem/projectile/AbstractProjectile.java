package com.github.jummes.supremeitem.projectile;

import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractProjectile {

    private final boolean projectilePresent;
    protected Target target;
    protected Source source;
    protected Location location;
    protected double gravity;
    protected double initialSpeed;
    protected List<Action> onEntityHitActions;
    protected List<Action> onBlockHitActions;
    protected List<Action> onProjectileTickActions;
    protected Entity entity;
    protected double hitBoxSize;
    protected int maxDistance;
    protected Vector initialDirection;
    private int projectileTask;
    private int counter;

    public AbstractProjectile(Target target, Source source, Location location, double gravity, double initialSpeed,
                              List<Action> onEntityHitActions, List<Action> onBlockHitActions,
                              List<Action> onProjectileTickActions, com.github.jummes.supremeitem.entity.Entity entity,
                              double hitBoxSize, int maxDistance) {
        this.target = target;
        this.source = source;
        this.location = location;
        this.gravity = gravity;
        this.initialSpeed = initialSpeed;
        this.onEntityHitActions = onEntityHitActions;
        this.onBlockHitActions = onBlockHitActions;
        this.onProjectileTickActions = onProjectileTickActions;
        this.entity = getEntity(entity);
        this.hitBoxSize = hitBoxSize;
        this.maxDistance = maxDistance;

        this.projectilePresent = this.entity != null;
        this.counter = 0;
    }

    public static boolean isProjectile(Entity entity) {
        return entity.getMetadata("projectile").stream().anyMatch(value ->
                Objects.equals(value.getOwningPlugin(), SupremeItem.getInstance()));
    }

    protected abstract Vector getInitialDirection();

    protected abstract void updateLocationAndDirection();

    public void run() {
        this.initialDirection = getInitialDirection();
        projectileTask = new BukkitRunnable() {
            @Override
            public void run() {

                if (projectileHitBlock()) {
                    onBlockHitActions.forEach(Action -> Action.execute(new LocationTarget(location), source));
                    remove();
                }

                List<LivingEntity> hitEntities = getHitEntities();
                if (!hitEntities.isEmpty()) {
                    hitEntities.forEach(livingEntity -> onEntityHitActions.forEach(action -> action.
                            execute(new EntityTarget(livingEntity), source)));
                    remove();
                }

                if (counter > maxDistance) {
                    remove();
                }

                onProjectileTickActions.forEach(action -> action.execute(new LocationTarget(location), source));

                if (projectilePresent)
                    entity.setVelocity(initialDirection);

                updateLocationAndDirection();

                if (gravity != 0) {
                    double newYSpeed = initialDirection.getY() - gravity * .05;
                    initialDirection.setY(newYSpeed);
                }

                counter++;
            }
        }.runTaskTimer(SupremeItem.getInstance(), 0, 1).getTaskId();
    }

    public Entity getEntity(com.github.jummes.supremeitem.entity.Entity entity) {
        Entity projectile = entity.spawnEntity(location, target, source);
        if (projectile != null) {
            projectile.setMetadata("projectile", new FixedMetadataValue(SupremeItem.getInstance(),
                    true));
            projectile.setGravity(false);
        }
        return projectile;
    }

    private void remove() {
        if (projectilePresent) {
            entity.remove();
        }
        Bukkit.getScheduler().cancelTask(projectileTask);
    }

    private boolean projectileHitBlock() {
        return location.getBlock().getType().isSolid();
    }

    private List<LivingEntity> getHitEntities() {
        if (hitBoxSize == 0) {
            return Lists.newArrayList();
        }
        return location.getWorld().getNearbyEntities(location, hitBoxSize, hitBoxSize, hitBoxSize).stream().
                filter(livingEntity -> livingEntity instanceof LivingEntity
                        && !livingEntity.equals(source.getCaster())
                        && !isProjectile(livingEntity)).
                map(entity -> (LivingEntity) entity).collect(Collectors.toList());
    }
}
