package com.github.jummes.supremeitem.placeholder.string;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lWorld Name Placeholder", description = "gui.placeholder.string.world-name.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmUyY2M0MjAxNWU2Njc4ZjhmZDQ5Y2NjMDFmYmY3ODdmMWJhMmMzMmJjZjU1OWEwMTUzMzJmYzVkYjUwIn19fQ==")
public class WorldNamePlaceholder extends StringPlaceholder {
    public WorldNamePlaceholder(boolean target) {
        super(target);
    }

    public WorldNamePlaceholder() {
        this(TARGET_DEFAULT);
    }

    public static WorldNamePlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new WorldNamePlaceholder(target);
    }

    @Override
    public StringPlaceholder clone() {
        return new WorldNamePlaceholder(target);
    }

    @Override
    public String computePlaceholder(Target target, Source source) {
        if (this.target) {
            if (target instanceof LocationTarget) {
                return ((LocationTarget) target).getTarget().getWorld().getName();
            } else if (target instanceof EntityTarget) {
                return ((EntityTarget) target).getTarget().getLocation().getWorld().getName();
            }
        }
        return source.getLocation().getWorld().getName();
    }

    @Override
    public String getName() {
        return String.format("%s World Name", target ? "Target" : "Source");
    }
}
