package com.github.jummes.supremeitem.placeholder.numeric.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.LocationSource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.Location;

@Enumerable.Displayable(name = "&c&lNumeric Location Placeholders", description = "gui.placeholder.double.location.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmUyY2M0MjAxNWU2Njc4ZjhmZDQ5Y2NjMDFmYmY3ODdmMWJhMmMzMmJjZjU1OWEwMTUzMzJmYzVkYjUwIn19fQ")
@Enumerable.Parent(classArray = {LocationXPlaceholder.class, LocationYPlaceholder.class, LocationZPlaceholder.class,
        LocationYawPlaceholder.class, LocationPitchPlaceholder.class})
public abstract class LocationPlaceholder extends NumericPlaceholder {

    public LocationPlaceholder(boolean target) {
        super(target);
    }

    public Location getLocation(Target target, Source source) {
        if (this.target) {
            if (target instanceof LocationTarget) {
                return ((LocationTarget) target).getTarget();
            } else if (target instanceof EntityTarget) {
                return ((EntityTarget) target).getTarget().getLocation();
            }
        }
        if (source instanceof LocationSource) {
            return ((LocationSource) source).getSource();
        } else if (source instanceof EntitySource) {
            return ((EntitySource) source).getSource().getLocation();
        }
        return null;
    }
}
