package com.github.jummes.supremeitem.placeholder.numeric.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.Location;

@Enumerable.Displayable(name = "&c&lNumeric Location Placeholders", description = "gui.placeholder.double.location.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmUyY2M0MjAxNWU2Njc4ZjhmZDQ5Y2NjMDFmYmY3ODdmMWJhMmMzMmJjZjU1OWEwMTUzMzJmYzVkYjUwIn19fQ")
@Enumerable.Parent(classArray = {LocationYPlaceholder.class, LocationXPlaceholder.class, LocationZPlaceholder.class,
        LocationYawPlaceholder.class, LocationPitchPlaceholder.class})
public abstract class LocationPlaceholder extends NumericPlaceholder {

    public LocationPlaceholder(boolean target) {
        super(target);
    }

    public Location getLocation(Target target, Source source) {
        if (this.target) {
            return target.getLocation();
        }
        return source.getLocation();
    }
}
