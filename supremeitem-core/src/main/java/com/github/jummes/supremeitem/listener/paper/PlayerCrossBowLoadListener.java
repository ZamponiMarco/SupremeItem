package com.github.jummes.supremeitem.listener.paper;

import com.github.jummes.supremeitem.listener.PlayerItemListener;
import com.github.jummes.supremeitem.skill.EntityCrossbowLoadSkill;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerCrossBowLoadListener implements Listener {

    @EventHandler
    public void onCrossbowLoad(EntityLoadCrossbowEvent e) {
        LivingEntity entity = e.getEntity();
        boolean cancelled = PlayerItemListener.executeSkill(entity, EntityCrossbowLoadSkill.class, skill -> true, entity);
        if (cancelled) {
            e.setCancelled(true);
        }
    }

}
