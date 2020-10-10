package com.github.jummes.supremeitem.listener;

import com.github.jummes.libs.core.Libs;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.skill.*;
import com.github.jummes.supremeitem.util.Utils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerItemListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        boolean cancelled = false;
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            Player p = e.getPlayer();
            if (e.getHand().equals(EquipmentSlot.HAND)) {
                ItemStack mainItem = p.getEquipment().getItemInMainHand();
                if (!Libs.getWrapper().getTagItem(mainItem, "supreme-item").equals("")) {
                    cancelled = executeInteractSkill(e, mainItem);
                }
            } else {
                ItemStack offItem = p.getEquipment().getItemInOffHand();
                if (!Libs.getWrapper().getTagItem(offItem, "supreme-item").equals("")) {
                    cancelled = executeInteractSkill(e, offItem);
                }
            }
        }
        e.setCancelled(cancelled);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        boolean cancelled;
        if (e.getEntity() instanceof LivingEntity) {
            LivingEntity damaged = (LivingEntity) e.getEntity();
            LivingEntity damager;
            if (e.getDamager() instanceof LivingEntity && e.getEntity() instanceof LivingEntity) {
                damager = (LivingEntity) e.getDamager();
            } else if (e.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) e.getDamager();
                damager = (LivingEntity) projectile.getShooter();
            } else {
                return;
            }
            cancelled = executeDamageEntitySkill(damager, damaged) || executeHitEntitySkill(damager, damaged);
            e.setCancelled(cancelled);
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if (player.isSneaking()) {
            boolean cancelled = executeSneakSkill(player);
        }
    }

    private boolean executeSneakSkill(Player player) {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        Utils.getEntityItems(player).forEach(item -> {
            try {
                UUID id = UUID.fromString(Libs.getWrapper().getTagItem(item, "supreme-item"));
                Item supremeItem = SupremeItem.getInstance().getItemManager().getById(id);
                if (supremeItem != null) {
                    supremeItem.getSkillSet().stream().filter(skill -> skill instanceof EntitySneakSkill).findFirst().
                            ifPresent(skill -> {
                                if (((EntitySneakSkill) skill).executeSkill(player, id, item).
                                        equals(DamageEntitySkill.SkillResult.CANCELLED)) {
                                    toReturn.set(true);
                                }
                            });
                }
            } catch (IllegalArgumentException ignored) {
            }
        });
        return toReturn.get();
    }

    /**
     * Execute interact skill
     *
     * @param e    The event
     * @param item The item bound to the skill
     * @return true if the event has to be cancelled, false otherwise
     */
    private boolean executeInteractSkill(PlayerInteractEvent e, ItemStack item) {
        AtomicBoolean cancelled = new AtomicBoolean(false);
        UUID id = UUID.fromString(Libs.getWrapper().getTagItem(item, "supreme-item"));
        Item supremeItem = SupremeItem.getInstance().getItemManager().getById(id);
        if (supremeItem != null) {
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                supremeItem.getSkillSet().stream().filter(skill -> skill instanceof RightClickSkill).findFirst().
                        ifPresent(skill -> {
                            if (((RightClickSkill) skill).executeSkill(e.getPlayer(), id, item).
                                    equals(Skill.SkillResult.CANCELLED)) {
                                cancelled.set(true);
                            }
                        });
            } else {
                supremeItem.getSkillSet().stream().filter(skill -> skill instanceof LeftClickSkill).findFirst().
                        ifPresent(skill -> {
                            if (((LeftClickSkill) skill).executeSkill(e.getPlayer(), id, item).
                                    equals(Skill.SkillResult.CANCELLED)) {
                                cancelled.set(true);
                            }
                        });
            }
        }
        return cancelled.get();
    }

    /**
     * Executes the damage entity skill
     *
     * @param damager The entity that damaged another entity
     * @param damaged The entity that was damaged
     * @return true if the event has to be cancelled, false otherwise
     */
    private boolean executeDamageEntitySkill(LivingEntity damager, LivingEntity damaged) {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        Utils.getEntityItems(damaged).forEach(item -> {
            try {
                UUID id = UUID.fromString(Libs.getWrapper().getTagItem(item, "supreme-item"));
                Item supremeItem = SupremeItem.getInstance().getItemManager().getById(id);
                if (supremeItem != null) {
                    supremeItem.getSkillSet().stream().filter(skill -> skill instanceof DamageEntitySkill).findFirst().
                            ifPresent(skill -> {
                                if (((DamageEntitySkill) skill).executeSkill(damaged, damager, id, item).
                                        equals(DamageEntitySkill.SkillResult.CANCELLED)) {
                                    toReturn.set(true);
                                }
                            });
                }
            } catch (IllegalArgumentException ignored) {
            }
        });
        return toReturn.get();
    }

    /**
     * Executes the hit entity skill
     *
     * @param damager The entity that damaged another entity
     * @param damaged The entity that was damaged
     * @return true if the event has to be cancelled, false otherwise
     */
    private boolean executeHitEntitySkill(LivingEntity damager, LivingEntity damaged) {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        Utils.getEntityItems(damager, false).forEach(item -> {
            try {
                UUID id = UUID.fromString(Libs.getWrapper().getTagItem(item, "supreme-item"));
                Item supremeItem = SupremeItem.getInstance().getItemManager().getById(id);
                if (supremeItem != null) {
                    supremeItem.getSkillSet().stream().filter(skill -> skill instanceof HitEntitySkill).findFirst().
                            ifPresent(skill -> {
                                if (((HitEntitySkill) skill).executeSkill(damager, damaged, id, item).
                                        equals(Skill.SkillResult.CANCELLED)) {
                                    toReturn.set(true);
                                }
                            });
                }
            } catch (IllegalArgumentException ignored) {
            }
        });
        return toReturn.get();
    }
}
