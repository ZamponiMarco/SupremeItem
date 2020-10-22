package com.github.jummes.supremeitem.hook;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardHook implements PluginHook {

    WorldGuardPlugin worldGuard;

    public WorldGuardHook() {
        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            this.worldGuard = WorldGuardPlugin.inst();
        }
    }

    @Override
    public boolean isEnabled() {
        return worldGuard != null;
    }

    public boolean canBuild(Player p, Location l) {
        if (!isEnabled()) {
            return true;
        }

        LocalPlayer localPlayer = worldGuard.wrapPlayer(p);
        com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(l);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        return query.testBuild(location, localPlayer);
    }
}
