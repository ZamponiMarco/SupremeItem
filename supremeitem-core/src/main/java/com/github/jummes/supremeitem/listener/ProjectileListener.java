package com.github.jummes.supremeitem.listener;

import com.github.jummes.supremeitem.projectile.Projectile;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ProjectileListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        if (Projectile.isProjectile(entity)) {
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void onFallingBlockLand(EntityChangeBlockEvent e) {
        Entity entity = e.getEntity();
        if (entity.getType() == EntityType.FALLING_BLOCK) {
            if (Projectile.isProjectile(entity)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHopperCatch(InventoryPickupItemEvent e) {
        Item entity = e.getItem();
        if (Projectile.isProjectile(entity)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        Entity entity = e.getEntity();
        if (Projectile.isProjectile(entity)) {
            entity.teleport(new Location(entity.getWorld(), 0, -50, 0),
                    PlayerTeleportEvent.TeleportCause.PLUGIN);
            entity.remove();
        }
    }

}
