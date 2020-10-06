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
@Enumerable.Displayable(name = "&c&lIs Sprinting Placeholder", description = "gui.placeholder.is-sprinting.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzU3Mzk0YzEzNTgwM2FmYTliOTVjODBhYmVhOGVhNjlhZTc0MGE4NGZkOWMwNGZlMmU0ZjYzZmRhM2M0MjQwMiJ9fX0=")
public class IsSprintingPlaceholder extends BooleanPlaceholder {
    public IsSprintingPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public IsSprintingPlaceholder(boolean target) {
        super(target);
    }

    public static IsSprintingPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.get("target");
        return new IsSprintingPlaceholder(target);
    }

    @Override
    public Boolean computePlaceholder(Target target, Source source) {
        if (this.target) {
            if (target instanceof EntityTarget) {
                LivingEntity livingEntity = ((EntityTarget) target).getTarget();
                if (livingEntity instanceof Player) {
                    Player player = (Player) livingEntity;
                    return player.isSprinting();
                }
            }
        }
        if (source instanceof EntitySource) {
            LivingEntity livingEntity = ((EntitySource) source).getSource();
            if (livingEntity instanceof Player) {
                Player player = (Player) livingEntity;
                return player.isSprinting();
            }
        }
        return false;
    }
}
