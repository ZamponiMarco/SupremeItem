package com.github.jummes.supremeitem.cooldown.bar;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.cooldown.CooldownInfo;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

@Enumerable.Child
public class ActionBar extends CooldownBar {

    protected static final String COOLDOWN_MESSAGE_FORMAT_DEFAULT = "&2Cooldown &6[%bar&6]";
    protected static final String COOLDOWN_MESSAGE_BAR_DEFAULT = "|";
    protected static final int COOLDOWN_MESSAGE_BAR_COUNT_DEFAULT = 30;

    private static final String FORMAT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZlMTk3MmYyY2ZhNGQzMGRjMmYzNGU4ZDIxNTM1OGMwYzU3NDMyYTU1ZjZjMzdhZDkxZTBkZDQ0MTkxYSJ9fX0=";
    private static final String BAR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjcxYjVjYTNhNjFiZWYyOTE2NWViMTI2NmI0MDVhYzI1OTE1NzJjMTZhNGIzOWNiMzZlZGFmNDZjODZjMDg4In19fQ==";
    private static final String BAR_COUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    private static final Map<Player, Integer> cooldownMessagesMap = new HashMap<>();

    @Serializable(headTexture = FORMAT_HEAD, description = "gui.skill.cooldown.format")
    @Serializable.Optional(defaultValue = "COOLDOWN_MESSAGE_FORMAT_DEFAULT")
    protected String cooldownMessageFormat;

    @Serializable(headTexture = BAR_HEAD, description = "gui.skill.cooldown.bar")
    @Serializable.Optional(defaultValue = "COOLDOWN_MESSAGE_BAR_DEFAULT")
    protected String cooldownMessageBar;

    @Serializable(headTexture = BAR_COUNT_HEAD, description = "gui.skill.cooldown.bar-count")
    @Serializable.Optional(defaultValue = "COOLDOWN_MESSAGE_BAR_COUNT_DEFAULT")
    protected int cooldownMessageBarCount;

    public ActionBar() {
        this(COOLDOWN_MESSAGE_FORMAT_DEFAULT, COOLDOWN_MESSAGE_BAR_DEFAULT, COOLDOWN_MESSAGE_BAR_COUNT_DEFAULT);
    }

    public ActionBar(String cooldownMessageFormat, String cooldownMessageBar, int cooldownMessageBarCount) {
        this.cooldownMessageFormat = cooldownMessageFormat;
        this.cooldownMessageBar = cooldownMessageBar;
        this.cooldownMessageBarCount = cooldownMessageBarCount;
    }

    public ActionBar(Map<String, Object> map) {
        this.cooldownMessageFormat = (String) map.getOrDefault("cooldownMessageFormat", COOLDOWN_MESSAGE_FORMAT_DEFAULT);
        this.cooldownMessageBar = (String) map.getOrDefault("cooldownMessageBar", COOLDOWN_MESSAGE_BAR_DEFAULT);
        this.cooldownMessageBarCount = (int) map.getOrDefault("cooldownMessageBarCount", COOLDOWN_MESSAGE_BAR_COUNT_DEFAULT);
    }

    @Override
    public BukkitRunnable sendProgress(Player player, CooldownInfo info, int cooldown) {
        return new BukkitRunnable() {
            @Override
            public void run() {

                if (info.getRemainingCooldown() == 0) {
                    this.cancel();
                }

                int progress = (int) Math.floor((info.getRemainingCooldown() / (double) cooldown) *
                        cooldownMessageBarCount);

                String sb = "&a" +
                        repeat(cooldownMessageBar, 30 - progress) +
                        "&c" +
                        repeat(cooldownMessageBar, progress) +
                        "&f";
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new ComponentBuilder(
                                MessageUtils.color(MessageUtils.color(cooldownMessageFormat.replaceAll("%bar", sb))))
                                .create());
            }
        };
    }

    private String repeat(String bar, int count) {
        return new String(new char[count]).replace("\0", bar);
    }

    @Override
    public void switchCooldownContext(Player p, CooldownInfo info, int cooldown) {
        if (cooldownMessagesMap.containsKey(p)) {
            Bukkit.getScheduler().cancelTask(cooldownMessagesMap.get(p));
        }
        cooldownMessagesMap.put(p, sendProgress(p, info, cooldown).
                runTaskTimer(SupremeItem.getInstance(), 0, 1).getTaskId());
    }
}
