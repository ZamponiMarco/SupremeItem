package com.github.jummes.supremeitem.cooldown.bar;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.cooldown.CooldownInfo;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lNo Cooldown Bar", description = "gui.skill.cooldown.bar.no-bar.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkMWFiYTczZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYzNiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ")
public class NoBar extends CooldownBar {

    public NoBar() {

    }

    public NoBar(Map<String, Object> map) {

    }

    @Override
    public BukkitRunnable sendProgress(Player player, CooldownInfo info, int cooldown) {
        return new BukkitRunnable() {
            @Override
            public void run() {

            }
        };
    }

    @Override
    public void switchCooldownContext(Player p, CooldownInfo info, int cooldown) {

    }
}
