package com.github.jummes.supremeitem.listener;

import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.skill.*;
import com.github.jummes.supremeitem.util.Utils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class PlayerItemListener implements Listener {

    public static Map<String, Object> executeSkill(LivingEntity caster, Class<? extends Skill> skillClass, Predicate<Skill>
            additionalPredicate, Object... args) {
        Map<String, Object> toReturn = new HashMap<>();
        List<ItemStack> items = Utils.getEntityItems(caster);
        IntStream.range(0, items.size()).forEach(i -> {
            Item item = SupremeItem.getInstance().getItemManager().getItemByItemStack(items.get(i));
            if (item != null) {
                item.getSkillSet().stream().filter(skill -> skillClass.isAssignableFrom(skill.getClass()) &&
                        skill.getAllowedSlots().contains(EquipmentSlot.values()[i]) && additionalPredicate.test(skill)).
                        forEach(skill -> mergeMaps(toReturn, skill.executeSkill(item.getId(), items.get(i), args)));
            }
        });
        return toReturn;
    }

    public static Map<String, Object> mergeMaps(Map<String, Object> first, Map<String, Object> second) {
        second.forEach((string, object) -> first.merge(string, object, (a, b) -> b));
        return first;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangeSlot(PlayerItemHeldEvent e) {
        if (e.getPlayer().getMetadata("toolbar-slot-change").stream().anyMatch(metadataValue ->
                Objects.equals(metadataValue.getOwningPlugin(), SupremeItem.getInstance()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            executeSkill(p, RightClickSkill.class, skill -> true, p);
        } else if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            executeSkill(p, LeftClickSkill.class, skill -> true, p);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
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

            Map<String, Object> map = new HashMap<>();

            if (e.getEntity().getMetadata("siattack").stream().noneMatch(metadataValue ->
                    Objects.equals(metadataValue.getOwningPlugin(), SupremeItem.getInstance()))) {
                mergeMaps(map, executeSkill(damager, HitEntitySkill.class, skill -> true, damager, damaged));
                mergeMaps(map, executeSkill(damaged, DamageEntitySkill.class, skill -> true, damaged, damager));
            } else {
                e.getEntity().removeMetadata("siattack", SupremeItem.getInstance());
            }
            if ((boolean) map.getOrDefault("cancelled", false)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        Map<String, Object> map = executeSkill(player, EntitySneakSkill.class, skill ->
                e.isSneaking() == ((EntitySneakSkill) skill).isOnActivate(), player);
        if ((boolean) map.getOrDefault("cancelled", false)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerSprint(PlayerToggleSprintEvent e) {
        Player player = e.getPlayer();
        Map<String, Object> map = executeSkill(player, EntitySprintSkill.class, skill ->
                e.isSprinting() == ((EntitySprintSkill) skill).isOnActivate(), player);
        if ((boolean) map.getOrDefault("cancelled", false)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerShoot(EntityShootBowEvent e) {
        LivingEntity entity = e.getEntity();
        Map<String, Object> map = executeSkill(entity, EntityBowShootSkill.class, skill -> true, entity);
        if ((boolean) map.getOrDefault("cancelled", false)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Map<String, Object> map = executeSkill(player, BlockPlaceSkill.class, skill -> true, player,
                e.getBlock().getLocation().clone().add(.5, .5, .5));
        if ((boolean) map.getOrDefault("cancelled", false)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBroken(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Map<String, Object> map = executeSkill(player, BlockBreakSkill.class, skill -> true, player,
                e.getBlock().getLocation().clone().add(.5, .5, .5));
        if ((boolean) map.getOrDefault("cancelled", false)) {
            e.setCancelled(true);
        }
    }

}
