package com.github.jummes.supremeitem.listener.paper;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.manager.ItemManager;
import com.github.jummes.supremeitem.skill.EntityEquipArmorSkill;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerChangeArmorListener implements Listener {

    @EventHandler
    public void onPlayerChangeArmor(PlayerArmorChangeEvent e) {
        Player entity = e.getPlayer();
        executeEquipSkill(entity, e.getOldItem(), e.getNewItem(),
                EquipmentSlot.valueOf(e.getSlotType().name()));
    }

    private void executeEquipSkill(LivingEntity entity, ItemStack oldItem, ItemStack newItem, EquipmentSlot slot) {
        if (oldItem != null && ItemManager.isSupremeItem(oldItem)) {
            UUID oldItemId = UUID.fromString(Libs.getWrapper().getTagItem(oldItem, "supreme-item"));
            Item oldSupremeItem = SupremeItem.getInstance().getItemManager().getItemById(oldItemId);
            if (oldSupremeItem != null) {
                oldSupremeItem.getSkillSet().stream().filter(skill -> skill instanceof EntityEquipArmorSkill &&
                        !((EntityEquipArmorSkill) skill).isOnEquip() && skill.getAllowedSlots().contains(slot)).findFirst().
                        ifPresent(skill -> skill.executeSkill(oldItemId, oldItem, entity));
            }
        }

        if (newItem != null && ItemManager.isSupremeItem(newItem)) {
            UUID newItemId = UUID.fromString(Libs.getWrapper().getTagItem(newItem, "supreme-item"));
            Item newSupremeItem = SupremeItem.getInstance().getItemManager().getItemById(newItemId);
            if (newSupremeItem != null) {
                newSupremeItem.getSkillSet().stream().filter(skill -> skill instanceof EntityEquipArmorSkill &&
                        ((EntityEquipArmorSkill) skill).isOnEquip() && skill.getAllowedSlots().contains(slot)).findFirst().
                        ifPresent(skill -> skill.executeSkill(newItemId, newItem, entity));
            }
        }
    }
}
