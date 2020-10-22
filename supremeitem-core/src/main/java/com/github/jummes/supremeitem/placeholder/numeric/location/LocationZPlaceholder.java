package com.github.jummes.supremeitem.placeholder.numeric.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.Location;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lLocation Z Placeholder", description = "gui.placeholder.double.location.z.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzA1ZjE4ZDQxNmY2OGU5YmQxOWQ1NWRmOWZhNzQyZWRmYmYxYTUyNWM4ZTI5ZjY1OWFlODMzYWYyMTdkNTM1In19fQ")
public class LocationZPlaceholder extends LocationPlaceholder {

    public LocationZPlaceholder(boolean target) {
        super(target);
    }

    public LocationZPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public static LocationZPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new LocationZPlaceholder(target);
    }

    @Override
    public NumericPlaceholder clone() {
        return new LocationZPlaceholder(target);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        Location location = getLocation(target, source);
        if (location != null) {
            return location.getZ();
        }
        return Double.NaN;
    }

    @Override
    public String getName() {
        return String.format("%s Z", target ? "Target" : "Source");
    }
}
