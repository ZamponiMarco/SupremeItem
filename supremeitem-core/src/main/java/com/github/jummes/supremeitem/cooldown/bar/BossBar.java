package com.github.jummes.supremeitem.cooldown.bar;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.cooldown.CooldownInfo;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Enumerable.Child
public class BossBar extends CooldownBar {

    protected static final String BAR_MESSAGE_DEFAULT = "&2Cooldown";

    private static final String FORMAT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZlMTk3MmYyY2ZhNGQzMGRjMmYzNGU4ZDIxNTM1OGMwYzU3NDMyYTU1ZjZjMzdhZDkxZTBkZDQ0MTkxYSJ9fX0=";

    private static final Map<Player, List<CooldownInfo>> cooldownsMap = new HashMap<>();

    @Serializable(headTexture = FORMAT_HEAD, description = "gui.skill.cooldown.format")
    @Serializable.Optional(defaultValue = "BAR_MESSAGE_DEFAULT")
    protected String barMessage;

    public BossBar() {
        this(BAR_MESSAGE_DEFAULT);
    }

    public BossBar(String barMessage) {
        this.barMessage = barMessage;
    }

    public BossBar(Map<String, Object> map) {
        this.barMessage = (String) map.getOrDefault("barMessage", BAR_MESSAGE_DEFAULT);
    }

    @Override
    public BukkitRunnable sendProgress(Player player, CooldownInfo info, int cooldown) {
        return new BukkitRunnable() {

            final org.bukkit.boss.BossBar bar = Bukkit.createBossBar(MessageUtils.color(barMessage), BarColor.BLUE,
                    BarStyle.SOLID);
            final double step = 1.0 / cooldown;

            {
                bar.addPlayer(player);
            }

            @Override
            public void run() {

                if (info.getRemainingCooldown() == 0) {
                    bar.removeAll();
                    cooldownsMap.get(player).remove(info);
                    this.cancel();
                }

                bar.setProgress(bar.getProgress() - step);

            }
        };
    }

    @Override
    public void switchCooldownContext(Player p, CooldownInfo info, int cooldown) {
        List<CooldownInfo> infos = cooldownsMap.computeIfAbsent(p, player -> Lists.newArrayList());
        if (!infos.contains(info)) {
            infos.add(info);
            sendProgress(p, info, cooldown).runTaskTimer(SupremeItem.getInstance(), 0, 1);
        }
    }
}
