package com.github.jummes.supremeitem.listener.paper;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.skill.EntityEquipArmorSkill;
import com.github.jummes.supremeitem.skill.Skill;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerChangeArmorListener implements Listener {

    @EventHandler
    public void onPlayerChangeArmor(PlayerArmorChangeEvent e) {
        Player entity = e.getPlayer();
        boolean cancelled = executeEquipSkill(entity, e.getOldItem(), e.getNewItem(),
                EquipmentSlot.valueOf(e.getSlotType().name()));
    }

    private boolean executeEquipSkill(LivingEntity entity, ItemStack oldItem, ItemStack newItem, EquipmentSlot slot) {
        AtomicBoolean toReturn = new AtomicBoolean(false);

        if (oldItem != null && Item.isSupremeItem(oldItem)) {
            UUID oldItemId = UUID.fromString(Libs.getWrapper().getTagItem(oldItem, "supreme-item"));
            Item oldSupremeItem = SupremeItem.getInstance().getItemManager().getById(oldItemId);
            if (oldSupremeItem != null) {
                oldSupremeItem.getSkillSet().stream().filter(skill -> skill instanceof EntityEquipArmorSkill &&
                        !((EntityEquipArmorSkill) skill).isOnEquip() && skill.getAllowedSlots().contains(slot)).findFirst().
                        ifPresent(skill -> {
                            if (((EntityEquipArmorSkill) skill).executeSkill(entity, oldItemId, oldItem).
                                    equals(Skill.SkillResult.CANCELLED)) {
                                toReturn.set(true);
                            }
                        });
            }
        }

        if (newItem != null && Item.isSupremeItem(newItem)) {
            UUID newItemId = UUID.fromString(Libs.getWrapper().getTagItem(newItem, "supreme-item"));
            Item newSupremeItem = SupremeItem.getInstance().getItemManager().getById(newItemId);
            if (newSupremeItem != null) {
                newSupremeItem.getSkillSet().stream().filter(skill -> skill instanceof EntityEquipArmorSkill &&
                        ((EntityEquipArmorSkill) skill).isOnEquip() && skill.getAllowedSlots().contains(slot)).findFirst().
                        ifPresent(skill -> {
                            if (((EntityEquipArmorSkill) skill).executeSkill(entity, newItemId, newItem).
                                    equals(Skill.SkillResult.CANCELLED)) {
                                toReturn.set(true);
                            }
                        });
            }
        }
        return toReturn.get();
    }
}
