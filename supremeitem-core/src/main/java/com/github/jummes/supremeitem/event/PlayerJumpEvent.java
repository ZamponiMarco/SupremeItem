package com.github.jummes.supremeitem.event;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerJumpEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private static final PlayerJumpEventListener listener = new PlayerJumpEventListener();

    private boolean cancelled;

    public PlayerJumpEvent(Player player) {
        super(player);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static void register(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    private static class PlayerJumpEventListener implements Listener {

        private Map<UUID, Integer> jumps = new HashMap<>();

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerJoin(PlayerJoinEvent e) {
            jumps.put(e.getPlayer().getUniqueId(), e.getPlayer().getStatistic(Statistic.JUMP));
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerQuit(PlayerQuitEvent e) {
            jumps.remove(e.getPlayer().getUniqueId());
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerMove(PlayerMoveEvent e) {
            Player player = e.getPlayer();

            if (e.getFrom().getY() < e.getTo().getY()) {
                int current = player.getStatistic(Statistic.JUMP);
                int last = jumps.getOrDefault(player.getUniqueId(), -1);

                if (last != current) {
                    jumps.put(player.getUniqueId(), current);

                    double yDif = (long) ((e.getTo().getY() - e.getFrom().getY()) * 1000) / 1000d;

                    PlayerJumpEvent jumpEvent = new PlayerJumpEvent(player);

                    if ((yDif < 0.035 || yDif > 0.037) && (yDif < 0.116 || yDif > 0.118)) {
                        Bukkit.getPluginManager().callEvent(jumpEvent);
                    }

                    if (jumpEvent.isCancelled()) {
                        e.getTo().setY(e.getFrom().getY());
                    }
                }
            }
        }
    }
}