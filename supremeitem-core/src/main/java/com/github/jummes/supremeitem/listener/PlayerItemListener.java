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
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class PlayerItemListener implements Listener {

    public static void executeSkill(LivingEntity caster, Class<? extends Skill> skillClass, Predicate<Skill>
            additionalPredicate, Map<String, Object> args) {
        Map<String, Object> toReturn = new HashMap<>();
        List<ItemStack> items = Utils.getEntityItems(caster);
        IntStream.range(0, items.size()).forEach(i -> {
            Item item = SupremeItem.getInstance().getItemManager().getItemByItemStack(items.get(i));
            if (item != null) {
                item.getSkillSet().stream().filter(skill -> skillClass.isAssignableFrom(skill.getClass()) &&
                        skill.getAllowedSlots().contains(Skill.slots.get(i)) && additionalPredicate.test(skill)).
                        forEach(skill -> skill.executeSkill(item.getId(), items.get(i), args));
            }
        });
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
        Map<String, Object> args = new HashMap<>();
        args.put("caster", p);
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            executeSkill(p, RightClickSkill.class, skill -> true, args);
        } else if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            executeSkill(p, LeftClickSkill.class, skill -> true, args);
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

            Map<String, Object> args = new HashMap<>();
            args.put("damager", damager);
            args.put("damaged", damaged);
            args.put("damage", e.getDamage());
            args.put("damageCause", e.getCause().name());

            if (e.getEntity().getMetadata("siattack").stream().noneMatch(metadataValue ->
                    Objects.equals(metadataValue.getOwningPlugin(), SupremeItem.getInstance()))) {
                executeSkill(damager, HitEntitySkill.class, skill -> true, args);
                executeSkill(damaged, DamageEntitySkill.class, skill -> true, args);
            } else {
                e.getEntity().removeMetadata("siattack", SupremeItem.getInstance());
            }
            if ((boolean) args.getOrDefault("cancelled", false)) {
                e.setCancelled(true);
            }

            double damage = (double) args.get("damage");

            e.setDamage(damage);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        Map<String, Object> args = new HashMap<>();
        args.put("caster", player);
        executeSkill(player, EntitySneakSkill.class, skill ->
                e.isSneaking() == ((EntitySneakSkill) skill).isOnActivate(), args);
        if ((boolean) args.getOrDefault("cancelled", false)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerSprint(PlayerToggleSprintEvent e) {
        Player player = e.getPlayer();
        Map<String, Object> args = new HashMap<>();
        args.put("caster", player);
        executeSkill(player, EntitySprintSkill.class, skill ->
                e.isSprinting() == ((EntitySprintSkill) skill).isOnActivate(), args);
        if ((boolean) args.getOrDefault("cancelled", false)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerShoot(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) e.getEntity().getShooter();
            Map<String, Object> args = new HashMap<>();
            args.put("caster", entity);
            executeSkill(entity, EntityShootProjectileSkill.class, skill -> true, args);
            if ((boolean) args.getOrDefault("cancelled", false)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Map<String, Object> args = new HashMap<>();
        args.put("caster", player);
        args.put("location", e.getBlock().getLocation().clone().add(.5, .5, .5));
        executeSkill(player, BlockPlaceSkill.class, skill -> true, args);
        if ((boolean) args.getOrDefault("cancelled", false)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBroken(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Map<String, Object> args = new HashMap<>();
        args.put("caster", player);
        args.put("location", e.getBlock().getLocation().clone().add(.5, .5, .5));
        executeSkill(player, BlockBreakSkill.class, skill -> true, args);
        if ((boolean) args.getOrDefault("cancelled", false)) {
            e.setCancelled(true);
        }
    }

}
