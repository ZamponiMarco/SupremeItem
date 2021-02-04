package com.github.jummes.supremeitem.listener.paper;

import com.github.jummes.supremeitem.listener.PlayerItemListener;
import com.github.jummes.supremeitem.skill.EntityCrossbowLoadSkill;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class PlayerCrossBowLoadListener implements Listener {

    @EventHandler
    public void onCrossbowLoad(EntityLoadCrossbowEvent e) {
        LivingEntity entity = e.getEntity();
        Map<String, Object> args = new HashMap<>();
        args.put("caster", entity);
        PlayerItemListener.executeSkill(entity, EntityCrossbowLoadSkill.class, skill -> true, args);
        if ((boolean) args.getOrDefault("cancelled", false)) {
            e.setCancelled(true);
        }
    }

}
