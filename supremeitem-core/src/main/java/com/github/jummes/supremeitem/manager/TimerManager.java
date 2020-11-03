package com.github.jummes.supremeitem.manager;

import com.github.jummes.libs.core.Libs;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.skill.TimerSkill;
import com.github.jummes.supremeitem.util.Utils;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class TimerManager {

    private final Map<Player, Set<TimerInfo>> timers;

    public TimerManager() {
        timers = new HashMap<>();
        Bukkit.getScheduler().runTaskTimer(SupremeItem.getInstance(), () -> {
            Set<Player> set = new HashSet<>(Bukkit.getOnlinePlayers());
            set.addAll(timers.keySet());
            set.forEach(player -> {
                Utils.getEntityItems(player).stream().filter(armor -> !Libs.getWrapper().getTagItem(armor, "supreme-item").
                        equals("")).forEach(armor -> addNewTimers(player, armor));
                removeTimers(player);
            });
        }, 0, 5);
    }

    public void removeTimers(Player player) {
        if (timers.containsKey(player)) {
            Iterator<TimerInfo> i = timers.get(player).iterator();
            while (i.hasNext()) {
                TimerInfo current = i.next();
                if (!player.isOnline() || Utils.getEntityItems(player).stream().noneMatch(armor ->
                        armor != null && !Libs.getWrapper().
                                getTagItem(armor, "supreme-item").equals("") &&
                                current.getItemId().equals(UUID.fromString(Libs.getWrapper().
                                        getTagItem(armor, "supreme-item"))))) {
                    Bukkit.getScheduler().cancelTask(current.task);
                    i.remove();
                }
            }
            if (timers.get(player).isEmpty()) {
                timers.remove(player);
            }
        }
    }

    public void addNewTimers(Player player, ItemStack armor) {
        if (armor != null) {
            UUID id = UUID.fromString(Libs.getWrapper().getTagItem(armor, "supreme-item"));
            Item supremeItem = SupremeItem.getInstance().getItemManager().getById(id);
            if (supremeItem != null) {
                TimerSkill timerSkill = (TimerSkill) supremeItem.getSkillSet().stream().filter(skill ->
                        skill instanceof TimerSkill).findFirst().orElse(null);
                if (timerSkill != null) {
                    startTimerTask(player, id, timerSkill);
                }
            }
        }
    }

    public void startTimerTask(Player player, UUID id, TimerSkill timerSkill) {
        if (!timers.containsKey(player)) {
            timers.put(player, Sets.newHashSet());
        }
        if (!timers.get(player).contains(new TimerInfo(id, 0))) {
            timers.get(player).add(new TimerInfo(id, Bukkit.getScheduler().runTaskTimer(SupremeItem.getInstance(),
                    () -> timerSkill.getOnWearerActions().forEach(action -> action.executeAction(new EntityTarget(player),
                            new EntitySource(player))), 0, timerSkill.getTimer()).getTaskId()));
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class TimerInfo {
        @EqualsAndHashCode.Include
        private UUID itemId;
        private int task;
    }
}
