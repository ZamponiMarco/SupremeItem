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
@Enumerable.Displayable(name = "&c&lIs Flying Placeholder", description = "gui.placeholder.is-flying.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmIxZDYxOTI4NDJlMTkyODg3Y2I0YjMzNjI0NzVlYzdhYTlmZWEzY2JkMWMyMzMyMjhkNWU0Y2YzZmNkYWYzIn19fQ==")
public class IsFlyingPlaceholder extends BooleanPlaceholder {
    public IsFlyingPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public IsFlyingPlaceholder(boolean target) {
        super(target);
    }

    public static IsFlyingPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new IsFlyingPlaceholder(target);
    }

    @Override
    public Boolean computePlaceholder(Target target, Source source) {
        if (this.target) {
            if (target instanceof EntityTarget) {
                LivingEntity livingEntity = ((EntityTarget) target).getTarget();
                if (livingEntity instanceof Player) {
                    Player player = (Player) livingEntity;
                    return player.isFlying();
                }
            }
        }
        if (source instanceof EntitySource) {
            LivingEntity livingEntity = ((EntitySource) source).getSource();
            if (livingEntity instanceof Player) {
                Player player = (Player) livingEntity;
                return player.isFlying();
            }
        }
        return false;
    }
}
