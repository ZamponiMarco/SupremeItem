package com.github.jummes.supremeitem.listener;

import com.github.jummes.libs.core.Libs;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.manager.ItemManager;
import com.github.jummes.supremeitem.skill.*;
import com.github.jummes.supremeitem.util.Utils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class PlayerItemListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangeSlot(PlayerItemHeldEvent e) {
        if (e.getPlayer().getMetadata("toolbar-slot-change").stream().anyMatch(metadataValue ->
                Objects.equals(metadataValue.getOwningPlugin(), SupremeItem.getInstance()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            Player p = e.getPlayer();
            executeInteractSkill(e);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        boolean cancelled = false;
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
            if (e.getEntity().getMetadata("siattack").stream().noneMatch(metadataValue ->
                    Objects.equals(metadataValue.getOwningPlugin(), SupremeItem.getInstance()))) {
                cancelled = executeDamageEntitySkill(damager, damaged) || executeHitEntitySkill(damager, damaged);
            } else {
                e.getEntity().removeMetadata("siattack", SupremeItem.getInstance());
            }
            if (cancelled) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        boolean cancelled = executeSneakSkill(player, e.isSneaking());
        if (cancelled) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerSprint(PlayerToggleSprintEvent e) {
        Player player = e.getPlayer();
        boolean cancelled = executeSprintSkill(player, e.isSprinting());
        if (cancelled) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerShoot(EntityShootBowEvent e) {
        LivingEntity entity = e.getEntity();
        boolean cancelled = executeBowSkill(entity);
        if (cancelled) {
            e.setCancelled(true);
        }
    }

    // Private methods

    private boolean executeSneakSkill(Player player, boolean activated) {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        List<ItemStack> items = Utils.getEntityItems(player);
        IntStream.range(0, items.size()).filter(i -> ItemManager.isSupremeItem(items.get(i))).forEach(i -> {
            UUID id = UUID.fromString(Libs.getWrapper().getTagItem(items.get(i), "supreme-item"));
            Item supremeItem = SupremeItem.getInstance().getItemManager().getItemById(id);
            if (supremeItem != null) {
                supremeItem.getSkillSet().stream().filter(skill -> skill instanceof EntitySneakSkill &&
                        activated == ((EntitySneakSkill) skill).isOnActivate() &&
                        skill.getAllowedSlots().contains(EquipmentSlot.values()[i])).findFirst().
                        ifPresent(skill -> {
                            if (((EntitySneakSkill) skill).executeSkill(player, id, items.get(i)).
                                    equals(Skill.SkillResult.CANCELLED)) {
                                toReturn.set(true);
                            }
                        });
            }
        });
        return toReturn.get();
    }

    private boolean executeSprintSkill(Player player, boolean activated) {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        List<ItemStack> items = Utils.getEntityItems(player);
        IntStream.range(0, items.size()).filter(i -> ItemManager.isSupremeItem(items.get(i))).forEach(i -> {
            UUID id = UUID.fromString(Libs.getWrapper().getTagItem(items.get(i), "supreme-item"));
            Item supremeItem = SupremeItem.getInstance().getItemManager().getItemById(id);
            if (supremeItem != null) {
                supremeItem.getSkillSet().stream().filter(skill -> skill instanceof EntitySprintSkill &&
                        activated == ((EntitySprintSkill) skill).isOnActivate() &&
                        skill.getAllowedSlots().contains(EquipmentSlot.values()[i])).findFirst().
                        ifPresent(skill -> {
                            if (((EntitySprintSkill) skill).executeSkill(player, id, items.get(i)).
                                    equals(Skill.SkillResult.CANCELLED)) {
                                toReturn.set(true);
                            }
                        });
            }
        });
        return toReturn.get();
    }

    private boolean executeBowSkill(LivingEntity entity) {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        List<ItemStack> items = Utils.getEntityItems(entity);
        IntStream.range(0, items.size()).filter(i -> ItemManager.isSupremeItem(items.get(i))).forEach(i -> {
            UUID id = UUID.fromString(Libs.getWrapper().getTagItem(items.get(i), "supreme-item"));
            Item supremeItem = SupremeItem.getInstance().getItemManager().getItemById(id);
            if (supremeItem != null) {
                supremeItem.getSkillSet().stream().filter(skill -> skill instanceof EntityBowShootSkill &&
                        skill.getAllowedSlots().contains(EquipmentSlot.values()[i])).findFirst().
                        ifPresent(skill -> {
                            if (((EntityBowShootSkill) skill).executeSkill(entity, id, items.get(i)).
                                    equals(Skill.SkillResult.CANCELLED)) {
                                toReturn.set(true);
                            }
                        });
            }
        });
        return toReturn.get();
    }

    /**
     * Execute interact skill
     *
     * @param e The event
     */
    private void executeInteractSkill(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action action = e.getAction();
        List<ItemStack> items = Utils.getEntityItems(p);
        IntStream.range(0, items.size()).filter(i -> ItemManager.isSupremeItem(items.get(i))).forEach(i ->
                executeItemInteractSkill(p, action, items.get(i), EquipmentSlot.values()[i]));
    }

    private void executeItemInteractSkill(Player p, Action action, ItemStack item, EquipmentSlot slot) {
        UUID id = UUID.fromString(Libs.getWrapper().getTagItem(item, "supreme-item"));
        Item supremeItem = SupremeItem.getInstance().getItemManager().getItemById(id);
        if (supremeItem != null) {
            if (action.equals(Action.RIGHT_CLICK_AIR)) {
                supremeItem.getSkillSet().stream().filter(skill -> skill instanceof RightClickSkill && skill.
                        getAllowedSlots().contains(slot)).findFirst().
                        ifPresent(skill -> ((RightClickSkill) skill).executeSkill(p, id, item));
            } else {
                supremeItem.getSkillSet().stream().filter(skill -> skill instanceof LeftClickSkill && skill.
                        getAllowedSlots().contains(slot)).findFirst().
                        ifPresent(skill -> ((LeftClickSkill) skill).executeSkill(p, id, item));
            }
        }
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
        List<ItemStack> items = Utils.getEntityItems(damaged);
        IntStream.range(0, items.size()).filter(i -> ItemManager.isSupremeItem(items.get(i))).forEach(i -> {
            UUID id = UUID.fromString(Libs.getWrapper().getTagItem(items.get(i), "supreme-item"));
            Item supremeItem = SupremeItem.getInstance().getItemManager().getItemById(id);
            if (supremeItem != null) {
                supremeItem.getSkillSet().stream().filter(skill -> skill instanceof DamageEntitySkill &&
                        skill.getAllowedSlots().contains(EquipmentSlot.values()[i])).findFirst().
                        ifPresent(skill -> {
                            if (((DamageEntitySkill) skill).executeSkill(damaged, damager, id, items.get(i)).
                                    equals(Skill.SkillResult.CANCELLED)) {
                                toReturn.set(true);
                            }
                        });
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
        List<ItemStack> items = Utils.getEntityItems(damager);
        IntStream.range(0, items.size()).filter(i -> ItemManager.isSupremeItem(items.get(i))).forEach(i -> {
            UUID id = UUID.fromString(Libs.getWrapper().getTagItem(items.get(i), "supreme-item"));
            Item supremeItem = SupremeItem.getInstance().getItemManager().getItemById(id);
            if (supremeItem != null) {
                supremeItem.getSkillSet().stream().filter(skill -> skill instanceof HitEntitySkill &&
                        skill.getAllowedSlots().contains(EquipmentSlot.values()[i])).findFirst().
                        ifPresent(skill -> {
                            if (((HitEntitySkill) skill).executeSkill(damager, damaged, id, items.get(i)).
                                    equals(Skill.SkillResult.CANCELLED)) {
                                toReturn.set(true);
                            }
                        });
            }
        });
        return toReturn.get();
    }
}
