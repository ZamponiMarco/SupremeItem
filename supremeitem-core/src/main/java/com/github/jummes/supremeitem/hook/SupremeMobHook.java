package com.github.jummes.supremeitem.hook;

import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.suprememob.SupremeMob;
import com.github.jummes.suprememob.api.SupremeMobAPI;
import com.github.jummes.suprememob.mob.Mob;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.List;

public class SupremeMobHook implements ExternalHook {

    private SupremeMobAPI api;

    public SupremeMobHook() {
        SupremeItem plugin = SupremeItem.getInstance();
        Bukkit.getServer().getPluginManager().registerEvents(new Listener(), plugin);
    }

    public List<String> getMobsList() {
        return api.getMobsNames();
    }

    public Mob getByName(String string) {
        return api.getByName(string);
    }

    public List<Mob> getMobs() {
        return api.getMobs();
    }

    @Override
    public boolean isEnabled() {
        return api != null;
    }

    public class Listener implements org.bukkit.event.Listener {

        @EventHandler
        public void onSupremeMobEnabled(PluginEnableEvent e) {
            if (e.getPlugin().getName().equals("SupremeMob")) {
                api = SupremeMob.getInstance().getApi();
            }
        }
    }
}
