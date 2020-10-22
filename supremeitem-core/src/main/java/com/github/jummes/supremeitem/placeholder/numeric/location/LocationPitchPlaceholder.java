package com.github.jummes.supremeitem.placeholder.numeric.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.Location;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lLocation Pitch Placeholder", description = "gui.placeholder.double.location.pitch.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19")
public class LocationPitchPlaceholder extends LocationPlaceholder {

    public LocationPitchPlaceholder(boolean target) {
        super(target);
    }

    public LocationPitchPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public static LocationPitchPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new LocationPitchPlaceholder(target);
    }

    @Override
    public NumericPlaceholder clone() {
        return new LocationPitchPlaceholder(target);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        Location location = getLocation(target, source);
        if (location != null) {
            return (double) location.getPitch();
        }
        return Double.NaN;
    }

    @Override
    public String getName() {
        return String.format("%s Pitch", target ? "Target" : "Source");
    }
}
