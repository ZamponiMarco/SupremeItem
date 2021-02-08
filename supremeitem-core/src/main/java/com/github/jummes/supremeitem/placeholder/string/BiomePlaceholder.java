package com.github.jummes.supremeitem.placeholder.string;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.Location;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lBiome Placeholder", description = "gui.placeholder.string.biome.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTE2YWI4ZjcwZDNkNjgyZjBiMTVlN2NhYzk0ZWM3NmQ3MjBhZDhiM2ExMTQ3ZmI1OWY4OTU2OWNjNTRkYTZjOCJ9fX0=")
public class BiomePlaceholder extends StringPlaceholder {
    public BiomePlaceholder(boolean target) {
        super(target);
    }

    public BiomePlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String computePlaceholder(Target target, Source source) {
        Location l;
        if (this.target) {
            l = target.getLocation();
        } else {
            l = source.getLocation();
        }
        return l.getBlock().getBiome().name();
    }

    @Override
    public String getName() {
        return String.format("%s World Biome", target ? "Target" : "Source");
    }

    @Override
    public StringPlaceholder clone() {
        return new BiomePlaceholder(target);
    }
}
