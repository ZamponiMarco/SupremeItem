package com.github.jummes.supremeitem.listener;

import com.github.jummes.libs.core.Libs;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.skill.DamageEntitySkill;
import com.github.jummes.supremeitem.skill.HitEntitySkill;
import com.github.jummes.supremeitem.skill.LeftClickSkill;
import com.github.jummes.supremeitem.skill.RightClickSkill;
import com.github.jummes.supremeitem.util.Utils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerItemListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            Player p = e.getPlayer();
            if (e.getHand().equals(EquipmentSlot.HAND)) {
                ItemStack mainItem = p.getEquipment().getItemInMainHand();
                if (!Libs.getWrapper().getTagItem(mainItem, "supreme-item").equals("")) {
                    executeInteractSkill(e, mainItem);
                }
            } else {
                ItemStack offItem = p.getEquipment().getItemInOffHand();
                if (!Libs.getWrapper().getTagItem(offItem, "supreme-item").equals("")) {
                    executeInteractSkill(e, offItem);
                }
            }
        }
    }

    private void executeInteractSkill(PlayerInteractEvent e, ItemStack item) {
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
            executeHitEntitySkill(damager, damaged);
            executeDamageEntitySkill(damager, damaged);
        } else if (e.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) e.getDamager();
            LivingEntity damager = (LivingEntity) projectile.getShooter();
            LivingEntity damaged = (LivingEntity) e.getEntity();
            executeHitEntitySkill(damager, damaged);
            executeDamageEntitySkill(damager, damaged);
        }
    }

    private void executeDamageEntitySkill(LivingEntity damager, LivingEntity damaged) {
        Utils.getEntityItems(damaged).forEach(item -> {
            try {
                UUID id = UUID.fromString(Libs.getWrapper().getTagItem(item, "supreme-item"));
                Item supremeItem = SupremeItem.getInstance().getItemManager().getById(id);
                if (supremeItem != null) {
                    supremeItem.getSkillSet().stream().filter(skill -> skill instanceof DamageEntitySkill).findFirst().
                            ifPresent(skill -> ((DamageEntitySkill) skill).executeSkill(damaged, damager, id, item));
                }
            } catch (IllegalArgumentException ignored) {

            }
        });
    }

    private void executeHitEntitySkill(LivingEntity damager, LivingEntity damaged) {
        Utils.getEntityItems(damager, false).forEach(item -> {
            try {
                UUID id = UUID.fromString(Libs.getWrapper().getTagItem(item, "supreme-item"));
                Item supremeItem = SupremeItem.getInstance().getItemManager().getById(id);
                if (supremeItem != null) {
                    supremeItem.getSkillSet().stream().filter(skill -> skill instanceof HitEntitySkill).findFirst().
                            ifPresent(skill -> ((HitEntitySkill) skill).executeSkill(damager, damaged, id, item));
                }
            } catch (IllegalArgumentException ignored) {

            }
        });
    }

}
