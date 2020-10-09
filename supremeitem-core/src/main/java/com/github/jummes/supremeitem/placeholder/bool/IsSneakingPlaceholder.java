package com.github.jummes.supremeitem.placeholder.bool;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lIs Sneaking Placeholder", description = "gui.placeholder.is-sneaking.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTQyMzI5YTljNDEwNDA4NDE5N2JkNjg4NjE1ODUzOTg0ZDM3ZTE3YzJkZDIzZTNlNDEyZGQ0MmQ3OGI5OGViIn19fQ==")
public class IsSneakingPlaceholder extends BooleanPlaceholder {
    public IsSneakingPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public IsSneakingPlaceholder(boolean target) {
        super(target);
    }

    public static IsSneakingPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new IsSneakingPlaceholder(target);
    }

    @Override
    public Boolean computePlaceholder(Target target, Source source) {
        if (this.target) {
            if (target instanceof EntityTarget) {
                LivingEntity livingEntity = ((EntityTarget) target).getTarget();
                if (livingEntity instanceof Player) {
                    Player player = (Player) livingEntity;
                    return player.isSneaking();
                }
            }
        }
        if (source instanceof EntitySource) {
            LivingEntity livingEntity = ((EntitySource) source).getSource();
            if (livingEntity instanceof Player) {
                Player player = (Player) livingEntity;
                return player.isSneaking();
            }
        }
        return false;
    }
}
