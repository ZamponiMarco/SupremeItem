package com.github.jummes.supremeitem.hook;

import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.suprememob.SupremeMob;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class SupremeMobHook implements PluginHook, Listener {

    private SupremeMob supremeMob;

    public SupremeMobHook() {
        SupremeItem plugin = SupremeItem.getInstance();
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSupremeMobEnabled(PluginEnableEvent e) {
        if (e.getPlugin().getName().equals("SupremeMob")) {
            this.supremeMob = SupremeMob.getInstance();
        }
    }

    @Override
    public boolean isEnabled() {
        return supremeMob != null;
    }
}
