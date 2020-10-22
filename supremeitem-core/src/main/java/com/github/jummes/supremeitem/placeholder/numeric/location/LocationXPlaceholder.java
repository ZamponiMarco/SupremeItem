package com.github.jummes.supremeitem.placeholder.numeric.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.Location;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lLocation X Placeholder", description = "gui.placeholder.double.location.x.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxZDZlZGE4M2VkMmMyNGRjZGNjYjFlMzNkZjM2OTRlZWUzOTdhNTcwMTIyNTViZmM1NmEzYzI0NGJjYzQ3NCJ9fX0")
public class LocationXPlaceholder extends LocationPlaceholder {

    public LocationXPlaceholder(boolean target) {
        super(target);
    }

    public LocationXPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public static LocationXPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new LocationXPlaceholder(target);
    }

    @Override
    public NumericPlaceholder clone() {
        return new LocationXPlaceholder(target);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        Location location = getLocation(target, source);
        if (location != null) {
            return location.getX();
        }
        return Double.NaN;
    }

    @Override
    public String getName() {
        return String.format("%s X", target ? "Target" : "Source");
    }
}
