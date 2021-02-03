package com.github.jummes.supremeitem.cooldown.bar;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.cooldown.CooldownInfo;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@Enumerable.Parent(classArray = {ActionBar.class, BossBar.class})
public abstract class CooldownBar implements Model {

    public abstract BukkitRunnable sendProgress(Player player, CooldownInfo info,
                                                int cooldown);

    public void switchCooldownContext(Player p, UUID id, UUID skillId, int cooldown) {
        switchCooldownContext(p, SupremeItem.getInstance().getCooldownManager().getCooldownInfo(p, id, skillId), cooldown);
    }

    public abstract void switchCooldownContext(Player p, CooldownInfo info, int cooldown);
}
