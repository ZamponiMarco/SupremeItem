package com.github.jummes.supremeitem.placeholder.numeric;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.entity.Player;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lHunger Placeholder", description = "gui.placeholder.hunger.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y5ZWQxMjM0NjYyYjg5OWNhMDhhMGRmZTc2YTk4ZTRhMjdkYTVkM2Q3NDY1NDM0ZjhiMzM1MGZmYjY1Njc3ZiJ9fX0=")
public class HungerPlaceholder extends NumericPlaceholder {

    public HungerPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public HungerPlaceholder(boolean target) {
        super(target);
    }

    public static HungerPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new HungerPlaceholder(target);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        if (this.target) {
            if (target instanceof EntityTarget && ((EntityTarget) target).getTarget() instanceof Player) {
                return (double) ((Player) ((EntityTarget) target).getTarget()).getFoodLevel();
            }
            return Double.NaN;
        }
        if (source instanceof EntitySource && ((EntitySource) source).getSource() instanceof Player) {
            return (double) ((Player) ((EntitySource) source).getSource()).getFoodLevel();
        }
        return Double.NaN;
    }
}
