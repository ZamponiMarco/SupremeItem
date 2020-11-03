package com.github.jummes.supremeitem.listener;

import com.github.jummes.supremeitem.SupremeItem;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Objects;

public class ProjectileListener implements Listener {

    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getMetadata("projectile").stream().anyMatch(value ->
                Objects.equals(value.getOwningPlugin(), SupremeItem.getInstance()))) {
            e.getDrops().clear();
        }
    }

    public void onFallingBlockLand(EntityChangeBlockEvent e) {
        if (e.getEntityType() == EntityType.FALLING_BLOCK) {
            if (e.getEntity().getMetadata("projectile").stream().anyMatch(value ->
                    Objects.equals(value.getOwningPlugin(), SupremeItem.getInstance()))) {
                e.setCancelled(true);
            }
        }
    }

}
