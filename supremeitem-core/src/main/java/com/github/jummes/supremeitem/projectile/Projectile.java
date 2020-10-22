package com.github.jummes.supremeitem.projectile;

import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.entity.NoEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

public class Projectile {

    public Projectile(Source source, Location l, double gravity, double initialSpeed, List<Action> onEntityHitActions,
                      List<Action> onBlockHitActions, List<Action> onProjectileTickActions,
                      com.github.jummes.supremeitem.entity.Entity entity, double hitBoxSize, double maxDistance) {
        Vector initialDirection = l.getDirection().multiply(initialSpeed).multiply(.05);
        BukkitRunnable runnable = new BukkitRunnable() {

            private final boolean projectilePresent = !(entity instanceof NoEntity);
            private Entity projectile;
            private int counter = 0;

            @Override
            public void run() {
                if (projectilePresent && projectile == null) {
                    projectile = entity.spawnEntity(l);
                }
                if (l.getBlock().getType().isBlock() && !l.getBlock().getType().equals(Material.AIR)
                        && l.getBlock().getType().isSolid()) {
                    onBlockHitActions.forEach(Action -> Action.executeAction(new LocationTarget(l), source));
                    remove();
                }
                List<LivingEntity> entities = l.getWorld().getNearbyEntities(l, hitBoxSize, hitBoxSize, hitBoxSize).stream().
                        filter(entity -> entity instanceof LivingEntity
                                && (!projectilePresent || !entity.equals(projectile))
                                && (source instanceof EntitySource && !entity.equals(source.getCaster()))).
                        map(entity -> (LivingEntity) entity).collect(Collectors.toList());
                if (!entities.isEmpty()) {
                    entities.forEach(entity -> onEntityHitActions.forEach(Action -> Action.
                            executeAction(new EntityTarget(entity), source)));
                    remove();
                } else if (counter > maxDistance) {
                    remove();
                }
                if (projectilePresent)
                    projectile.setVelocity(initialDirection);
                onProjectileTickActions.forEach(action -> action.executeAction(new LocationTarget(l), source));
                l.add(initialDirection);
                if (gravity > 0) {
                    double newYSpeed = initialDirection.getY() - gravity * .05;
                    initialDirection.setY(newYSpeed);
                }
                counter++;
            }

            private void remove() {
                if (projectilePresent) {
                    projectile.remove();
                }
                this.cancel();
            }
        };
        runnable.runTaskTimer(SupremeItem.getInstance(), 0, 1);
    }
}
