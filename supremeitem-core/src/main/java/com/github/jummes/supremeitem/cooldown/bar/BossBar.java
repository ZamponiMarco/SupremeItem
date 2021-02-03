package com.github.jummes.supremeitem.cooldown.bar;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.cooldown.CooldownInfo;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lBoss Cooldown Bar", description = "gui.skill.cooldown.bar.boss.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWUyODBjZWZlOTQ2OTExZWE5MGU4N2RlZDFiM2UxODMzMGM2M2EyM2FmNTEyOWRmY2ZlOWE4ZTE2NjU4ODA0MSJ9fX0=")
public class BossBar extends CooldownBar {

    protected static final String BAR_MESSAGE_DEFAULT = "&2Cooldown";
    protected static final BarColor BAR_COLOR_DEFAULT = BarColor.BLUE;
    protected static final BarStyle BAR_STYLE_DEFAULT = BarStyle.SOLID;

    private static final String FORMAT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZlMTk3MmYyY2ZhNGQzMGRjMmYzNGU4ZDIxNTM1OGMwYzU3NDMyYTU1ZjZjMzdhZDkxZTBkZDQ0MTkxYSJ9fX0=";
    private static final String COLOR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmVjNTk2M2UxZjc4ZjJmMDU5NDNmNGRkMzIyMjQ2NjEzNzRjMjIwZWNmZGUxZTU0NzU0ZjVlZTFlNTU1ZGQifX19";
    private static final String STYLE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTRiZGE5ZWEyZmNlYWFhOTQ1YzM4ZWQ0N2IxNDMxZDM5OTY3M2I4M2IyNzY5Njc0YWU5ZDllODNjM2ZmZDQzIn19fQ==";

    private static final Map<Player, List<CooldownInfo>> cooldownsMap = new HashMap<>();

    @Serializable(headTexture = FORMAT_HEAD, description = "gui.skill.cooldown.bar.boss.message")
    @Serializable.Optional(defaultValue = "BAR_MESSAGE_DEFAULT")
    private String barMessage;

    @Serializable(headTexture = COLOR_HEAD, description = "gui.skill.cooldown.bar.boss.color", fromList = "getColors",
            fromListMapper = "colorsMapper", stringValue = true)
    private BarColor color;

    @Serializable(headTexture = STYLE_HEAD, description = "gui.skill.cooldown.bar.boss.style", fromList = "getStyles",
            fromListMapper = "stylesMapper", stringValue = true)
    private BarStyle style;

    public BossBar() {
        this(BAR_MESSAGE_DEFAULT, BAR_COLOR_DEFAULT, BAR_STYLE_DEFAULT);
    }

    public BossBar(String barMessage, BarColor color, BarStyle style) {
        this.barMessage = barMessage;
        this.color = color;
        this.style = style;
    }

    public BossBar(Map<String, Object> map) {
        this.barMessage = (String) map.getOrDefault("barMessage", BAR_MESSAGE_DEFAULT);
        this.color = BarColor.valueOf((String) map.getOrDefault("color", "BLUE"));
        this.style = BarStyle.valueOf((String) map.getOrDefault("style", "SOLID"));
    }

    public static List<Object> getColors(ModelPath<?> path) {
        return Lists.newArrayList(BarColor.values());
    }

    public static Function<Object, ItemStack> colorsMapper() {
        return obj -> ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(COLOR_HEAD), ((BarColor) obj).name(),
                Lists.newArrayList());
    }

    public static List<Object> getStyles(ModelPath<?> path) {
        return Lists.newArrayList(BarStyle.values());
    }

    public static Function<Object, ItemStack> stylesMapper() {
        return obj -> ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(STYLE_HEAD), ((BarStyle) obj).name(),
                Lists.newArrayList());
    }

    @Override
    public BukkitRunnable sendProgress(Player player, CooldownInfo info, int cooldown) {
        return new BukkitRunnable() {

            final org.bukkit.boss.BossBar bar = Bukkit.createBossBar(MessageUtils.color(barMessage), color, style);
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
