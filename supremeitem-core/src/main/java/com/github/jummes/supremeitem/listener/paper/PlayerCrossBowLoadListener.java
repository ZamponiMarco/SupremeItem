package com.github.jummes.supremeitem.listener.paper;

import com.github.jummes.libs.core.Libs;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.manager.ItemManager;
import com.github.jummes.supremeitem.skill.EntityCrossbowLoadSkill;
import com.github.jummes.supremeitem.skill.Skill;
import com.github.jummes.supremeitem.util.Utils;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class PlayerCrossBowLoadListener implements Listener {

    @EventHandler
    public void onCrossbowLoad(EntityLoadCrossbowEvent e) {
        LivingEntity entity = e.getEntity();
        boolean cancelled = executeCrossbowSkill(entity);
        if (cancelled) {
            e.setCancelled(true);
        }
    }

    private boolean executeCrossbowSkill(LivingEntity entity) {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        List<ItemStack> items = Utils.getEntityItems(entity);
        IntStream.range(0, items.size()).filter(i -> ItemManager.isSupremeItem(items.get(i))).forEach(i -> {
            UUID id = UUID.fromString(Libs.getWrapper().getTagItem(items.get(i), "supreme-item"));
            Item supremeItem = SupremeItem.getInstance().getItemManager().getItemById(id);
            if (supremeItem != null) {
                supremeItem.getSkillSet().stream().filter(skill -> skill instanceof EntityCrossbowLoadSkill &&
                        skill.getAllowedSlots().contains(EquipmentSlot.values()[i])).findFirst().
                        ifPresent(skill -> {
                            if (((EntityCrossbowLoadSkill) skill).executeSkill(entity, id, items.get(i)).
                                    equals(Skill.SkillResult.CANCELLED)) {
                                toReturn.set(true);
                            }
                        });
            }
        });
        return toReturn.get();
    }

}
