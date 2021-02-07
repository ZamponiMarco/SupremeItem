package com.github.jummes.supremeitem.placeholder.bool;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.Location;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lIs Weather Clear", description = "gui.placeholder.boolean.clear.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTI5MmQxNzI2MTcxYWJhYmY3M2Y4NDQxMTU0Y2Y3YjcyZWUyZTBlNDY0NGQ2ZWUwODM4ZDc2MGRjMzQ4OWM5MiJ9fX0=")
public class IsWeatherClearPlaceholder extends BooleanPlaceholder {

    public IsWeatherClearPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public IsWeatherClearPlaceholder(boolean target) {
        super(target);
    }

    public IsWeatherClearPlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Boolean computePlaceholder(Target target, Source source) {
        Location location;
        if (this.target) {
            location = target.getLocation();
        } else {
            location = source.getLocation();
        }
        return location.getWorld().isClearWeather();
    }

    @Override
    public String getName() {
        return String.format("%s World Weather is Clear", target ? "Target" : "Source");
    }

    @Override
    public BooleanPlaceholder clone() {
        return new IsWeatherClearPlaceholder(target);
    }
}
