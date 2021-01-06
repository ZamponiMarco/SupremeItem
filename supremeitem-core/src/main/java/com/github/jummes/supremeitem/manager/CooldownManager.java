package com.github.jummes.supremeitem.manager;

import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.skill.CooldownSkill;
import com.github.jummes.supremeitem.skill.RightClickSkill;
import com.github.jummes.supremeitem.skill.Skill;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CooldownManager {

    private final Map<LivingEntity, List<CooldownInfo>> cooldowns;
    private final Map<Player, Integer> cooldownMessagesMap;

    public CooldownManager() {
        cooldowns = new HashMap<>();
        cooldownMessagesMap = new HashMap<>();
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
            switchCooldownContext((Player) e, info, cooldownOptions);
        }
    }

    public void switchCooldownContext(Player p, UUID id, Class<? extends Skill> skill,
                                      CooldownSkill.CooldownOptions cooldownOptions) {
        switchCooldownContext(p, getCooldownInfo(p, id, skill), cooldownOptions);
    }

    private void switchCooldownContext(Player p, CooldownInfo info, CooldownSkill.CooldownOptions cooldownOptions) {
        if (cooldownOptions.isCooldownMessage()) {
            if (cooldownMessagesMap.containsKey(p)) {
                Bukkit.getScheduler().cancelTask(cooldownMessagesMap.get(p));
            }
            cooldownMessagesMap.put(p, sendProgressMessage(p, info, cooldownOptions).
                    runTaskTimer(SupremeItem.getInstance(),
                            0, 1).getTaskId());
        }
    }

    private BukkitRunnable sendProgressMessage(Player player, CooldownInfo info,
                                               CooldownSkill.CooldownOptions cooldownOptions) {
        return new BukkitRunnable() {
            @Override
            public void run() {

                if (info.getRemainingCooldown() == 0) {
                    this.cancel();
                }

                int progress = (int) Math.floor((info.getRemainingCooldown() / (double) cooldownOptions.getCooldown()) *
                        cooldownOptions.getCooldownMessageBarCount());

                String sb = "&a" +
                        repeat(cooldownOptions.getCooldownMessageBar(), 30 - progress) +
                        "&c" +
                        repeat(cooldownOptions.getCooldownMessageBar(), progress) +
                        "&f";
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new ComponentBuilder(
                                MessageUtils.color(MessageUtils.color(cooldownOptions.
                                        getCooldownMessageFormat().replaceAll("%bar", sb))))
                                .create());
            }
        };
    }

    private String repeat(String bar, int count) {
        return new String(new char[count]).replace("\0", bar);
    }

    public CooldownInfo getCooldownInfo(LivingEntity e, UUID id, Class<? extends Skill> skill) {
        if (cooldowns.containsKey(e)) {
            return cooldowns.get(e).stream().filter(info -> info.getItemId().equals(id) && info.getSkill().equals(skill)).
                    findFirst().orElse(new CooldownInfo(id, 0, RightClickSkill.class));
        }
        return new CooldownInfo(id, 0, RightClickSkill.class);
    }

    public int getCooldown(LivingEntity e, UUID id, Class<? extends Skill> skill) {
        CooldownInfo info = getCooldownInfo(e, id, skill);
        return info.getRemainingCooldown();
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class CooldownInfo {
        @EqualsAndHashCode.Include
        private UUID itemId;
        private int remainingCooldown;
        @EqualsAndHashCode.Include
        private Class<? extends Skill> skill;
    }

}
