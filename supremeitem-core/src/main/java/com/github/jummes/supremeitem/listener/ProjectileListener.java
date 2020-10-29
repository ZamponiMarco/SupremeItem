package com.github.jummes.supremeitem.listener;

import com.github.jummes.supremeitem.SupremeItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class ProjectileListener implements Listener {


    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getMetadata("projectile").stream().anyMatch(value -> value.getOwningPlugin().
                equals(SupremeItem.getInstance()))) {
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void onFallingBlockLand(EntityChangeBlockEvent e) {
        if (e.getEntityType() == EntityType.FALLING_BLOCK) {
            if (e.getEntity().getMetadata("projectile").stream().anyMatch(value -> value.getOwningPlugin().
                    equals(SupremeItem.getInstance()))) {
                e.setCancelled(true);
            }
        }
    }

}
