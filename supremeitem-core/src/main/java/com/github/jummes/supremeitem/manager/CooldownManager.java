package com.github.jummes.supremeitem.manager;

import com.github.jummes.supremeitem.SupremeItem;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import java.util.*;

public class CooldownManager {

    private Map<LivingEntity, List<CooldownInfo>> cooldowns;

    public CooldownManager() {
        cooldowns = new HashMap<>();
        Bukkit.getScheduler().runTaskTimer(SupremeItem.getInstance(), () -> {
            Iterator<Map.Entry<LivingEntity, List<CooldownInfo>>> i = cooldowns.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry<LivingEntity, List<CooldownInfo>> entry = i.next();
                entry.getValue().forEach(info -> {
                    info.setRemainingCooldown(info.getRemainingCooldown() - 1);
                });
                entry.getValue().removeIf(info -> info.getRemainingCooldown() == 0);
                if (entry.getValue().isEmpty()) {
                    i.remove();
                }
            }
        }, 0, 1);
    }

    public void addCooldown(LivingEntity e, CooldownInfo info) {
        if (cooldowns.get(e) == null) {
            cooldowns.put(e, Lists.newArrayList());
        }
        cooldowns.get(e).add(info);
    }

    public int getCooldown(LivingEntity e, UUID id) {
        if (cooldowns.containsKey(e)) {
            return cooldowns.get(e).stream().filter(info -> info.getItemId().equals(id)).findFirst().
                    orElse(new CooldownInfo(id, 0)).getRemainingCooldown();
        }
        return 0;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class CooldownInfo {
        @EqualsAndHashCode.Include
        private UUID itemId;
        private int remainingCooldown;
    }

}
