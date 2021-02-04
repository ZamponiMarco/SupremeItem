package com.github.jummes.supremeitem.listener.paper;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.listener.PlayerItemListener;
import com.github.jummes.supremeitem.skill.EntityJumpSkill;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlayerJumpListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJump(PlayerJumpEvent e) {
        if (e.getPlayer().getMetadata("jump").stream().anyMatch(metadataValue ->
                Objects.equals(metadataValue.getOwningPlugin(), SupremeItem.getInstance()))) {
            e.setCancelled(true);
        }

        Player player = e.getPlayer();
        Map<String, Object> args = new HashMap<>();
        args.put("caster", player);
        PlayerItemListener.executeSkill(player, EntityJumpSkill.class, skill -> true, args);
        if ((boolean) args.getOrDefault("cancelled", false)) {
            e.setCancelled(true);
        }
    }

}
