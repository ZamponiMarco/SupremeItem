package com.github.jummes.supremeitem.manager;

import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.cooldown.CooldownInfo;
import com.github.jummes.supremeitem.skill.CooldownSkill;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

public class CooldownManager {

    private final Map<LivingEntity, List<CooldownInfo>> cooldowns;

    public CooldownManager() {
        cooldowns = new HashMap<>();
        Bukkit.getScheduler().runTaskTimer(SupremeItem.getInstance(), getCooldownProcess(), 0, 1);
    }

    private Runnable getCooldownProcess() {
        return () -> {
            Iterator<Map.Entry<LivingEntity, List<CooldownInfo>>> i = cooldowns.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry<LivingEntity, List<CooldownInfo>> entry = i.next();
                entry.getValue().forEach(info -> info.setRemainingCooldown(info.getRemainingCooldown() - 1));
                entry.getValue().removeIf(info -> info.getRemainingCooldown() == 0);
                if (entry.getValue().isEmpty()) {
                    i.remove();
                }
            }
        };
    }

    public void addCooldown(LivingEntity e, CooldownInfo info, CooldownSkill.CooldownOptions cooldownOptions) {
        cooldowns.computeIfAbsent(e, k -> Lists.newArrayList());
        cooldowns.get(e).add(info);
        if (e instanceof Player) {
            cooldownOptions.getBar().switchCooldownContext((Player) e, info, cooldownOptions.getCooldown());
        }
    }

    public CooldownInfo getCooldownInfo(LivingEntity e, UUID id, UUID skillId) {
        if (cooldowns.containsKey(e)) {
            return cooldowns.get(e).stream().filter(info -> info.getItemId().equals(id) && info.getSkillId().equals(skillId)).
                    findFirst().orElse(new CooldownInfo(id, 0, UUID.randomUUID()));
        }
        return new CooldownInfo(id, 0, UUID.randomUUID());
    }

    public int getCooldown(LivingEntity e, UUID id, UUID skillId) {
        CooldownInfo info = getCooldownInfo(e, id, skillId);
        return info.getRemainingCooldown();
    }

}
