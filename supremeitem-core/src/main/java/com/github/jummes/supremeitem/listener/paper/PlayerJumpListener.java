package com.github.jummes.supremeitem.listener.paper;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.github.jummes.supremeitem.SupremeItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class PlayerJumpListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJump(PlayerJumpEvent e) {
        if (e.getPlayer().getMetadata("jump").stream().anyMatch(metadataValue ->
                Objects.equals(metadataValue.getOwningPlugin(), SupremeItem.getInstance()))) {
            e.setCancelled(true);
        }
    }

}
