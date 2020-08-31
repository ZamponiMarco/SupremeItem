package com.github.jummes.supremeitem.listener;

import com.github.jummes.libs.core.Libs;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.item.skill.HitEntitySkill;
import com.github.jummes.supremeitem.item.skill.LeftClickSkill;
import com.github.jummes.supremeitem.item.skill.RightClickSkill;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;

import java.util.UUID;

public class PlayerItemListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            Player p = e.getPlayer();
            if (e.getHand().equals(EquipmentSlot.HAND)) {
                ItemStack mainItem = p.getEquipment().getItemInMainHand();
                if (!Libs.getWrapper().getTagItem(mainItem, "supreme-item").equals("")) {
                    executeInteractSkill(e, p.getMainHand(), mainItem);
                }
            } else {
                ItemStack offItem = p.getEquipment().getItemInOffHand();
                MainHand hand = p.getMainHand().equals(MainHand.RIGHT) ? MainHand.LEFT : MainHand.RIGHT;
                if (!Libs.getWrapper().getTagItem(offItem, "supreme-item").equals("")) {
                    executeInteractSkill(e, hand, offItem);
                }
            }
        }
    }

    private void executeInteractSkill(PlayerInteractEvent e, MainHand hand, ItemStack item) {
        UUID id = UUID.fromString(Libs.getWrapper().getTagItem(item, "supreme-item"));
        Item supremeItem = SupremeItem.getInstance().getItemManager().getById(id);
        if (supremeItem != null) {
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                supremeItem.getSkillSet().stream().filter(skill -> skill instanceof RightClickSkill).findFirst().
                        ifPresent(skill -> ((RightClickSkill) skill).executeSkill(e.getPlayer(), id, item));
            } else {
                supremeItem.getSkillSet().stream().filter(skill -> skill instanceof LeftClickSkill).findFirst().
                        ifPresent(skill -> ((LeftClickSkill) skill).executeSkill(e.getPlayer(), id, item));
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof LivingEntity && e.getEntity() instanceof LivingEntity) {
            LivingEntity damager = (LivingEntity) e.getDamager();
            LivingEntity damaged = (LivingEntity) e.getEntity();
            ItemStack item = damager.getEquipment().getItemInMainHand();
            try {
                UUID id = UUID.fromString(Libs.getWrapper().getTagItem(item, "supreme-item"));
                Item supremeItem = SupremeItem.getInstance().getItemManager().getById(id);
                if (supremeItem != null) {
                    supremeItem.getSkillSet().stream().filter(skill -> skill instanceof HitEntitySkill).findFirst().
                            ifPresent(skill -> ((HitEntitySkill) skill).executeSkill(damager, damaged, id, item));
                }
            } catch (IllegalArgumentException ex) {

            }
        }
    }

}
