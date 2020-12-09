package com.github.jummes.supremeitem.placeholder.numeric.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.Location;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lLocation Yaw Placeholder", description = "gui.placeholder.double.location.yaw.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0")
public class LocationYawPlaceholder extends LocationPlaceholder {

    public LocationYawPlaceholder(boolean target) {
        super(target);
    }

    public LocationYawPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public LocationYawPlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public NumericPlaceholder clone() {
        return new LocationYawPlaceholder(target);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        Location location = getLocation(target, source);
        if (location != null) {
            return (double) location.getYaw();
        }
        return Double.NaN;
    }

    @Override
    public String getName() {
        return String.format("%s Yaw", target ? "Target" : "Source");
    }
}
