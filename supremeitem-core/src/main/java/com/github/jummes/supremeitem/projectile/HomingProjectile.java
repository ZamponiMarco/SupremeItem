package com.github.jummes.supremeitem.projectile;

import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomingProjectile {

    public HomingProjectile(Target target, Source source, Location l, LivingEntity entityTarget, double gravity, double initialSpeed, List<Action> onEntityHitActions,
                            List<Action> onBlockHitActions, List<Action> onProjectileTickActions,
                            com.github.jummes.supremeitem.entity.Entity entity, double hitBoxSize, double maxDistance,
                            double projectileSpread, double turnSpeed) {
        Vector initialDirection = l.getDirection().multiply(initialSpeed).multiply(.05);
        double spread = Math.toRadians(projectileSpread);
        initialDirection.rotateAroundX((Math.random() - 0.5) * spread);
        initialDirection.rotateAroundY((Math.random() - 0.5) * spread);
        initialDirection.rotateAroundZ((Math.random() - 0.5) * spread);
        BukkitRunnable runnable = new BukkitRunnable() {

            private final Entity projectile = getEntity();
            private final boolean projectilePresent = projectile != null;
            private int counter = 0;

            public Entity getEntity() {
                Entity projectile = entity.spawnEntity(l, target, source);
                if (projectile != null) {
                    projectile.setMetadata("projectile", new FixedMetadataValue(SupremeItem.getInstance(),
                            true));
                    projectile.setGravity(false);
                }
                return projectile;
            }

            @Override
            public void run() {

                if (l.getBlock().getType().isBlock() && !l.getBlock().getType().equals(Material.AIR)
                        && l.getBlock().getType().isSolid()) {
                    onBlockHitActions.forEach(Action -> Action.execute(new LocationTarget(l), source));
                    remove();
                }

                List<LivingEntity> entities = l.getWorld().getNearbyEntities(l, hitBoxSize, hitBoxSize, hitBoxSize).stream().
                        filter(entity -> entity instanceof LivingEntity
                                && (!Objects.equals(projectile, entity))
                                && (source instanceof EntitySource && !entity.equals(source.getCaster()))
                                && !isProjectile(entity)).
                        map(entity -> (LivingEntity) entity).collect(Collectors.toList());
                if (!entities.isEmpty()) {
                    entities.forEach(entity -> onEntityHitActions.forEach(Action -> Action.
                            execute(new EntityTarget(entity), source)));
                    remove();
                } else if (counter > maxDistance) {
                    remove();
                }

                onProjectileTickActions.forEach(action -> action.execute(new LocationTarget(l), source));

                if (projectilePresent)
                    projectile.setVelocity(initialDirection);

                l.add(initialDirection);

                // Modify direction

                double magnitude = initialDirection.length();
                initialDirection.normalize();
                Vector targetDifference = entityTarget.getEyeLocation().clone().toVector().subtract(l.clone().toVector());
                targetDifference.normalize();

                lerp(initialDirection, targetDifference, turnSpeed);
                initialDirection.multiply(magnitude);

                if (gravity > 0) {
                    double newYSpeed = initialDirection.getY() - gravity * .05;
                    initialDirection.setY(newYSpeed);
                }
                counter++;
            }

            private Vector lerp(Vector start, Vector end, double percent) {
                return start.add(end.clone().subtract(start).multiply(percent)).normalize();
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

    public static boolean isProjectile(Entity entity) {
        return entity.getMetadata("projectile").stream().anyMatch(value ->
                Objects.equals(value.getOwningPlugin(), SupremeItem.getInstance()));
    }
}
