package com.github.jummes.supremeitem.placeholder.bool;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Map;

@Enumerable.Displayable(name = "&c&lPlayer Properties", description = "gui.placeholder.boolean.player.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWMxNjcxOWEzNGVkNmU2NWNmMjE2Njk2NThkNDUzOTA2MGQ0ZDFhMjM2MmJhZDNjYzJkZTU3M2M5ZWM2ZjIifX19")
@Enumerable.Parent(classArray = {IsSneakingPlaceholder.class, IsFlyingPlaceholder.class, IsSprintingPlaceholder.class,
        HasPermissionPlaceholder.class})
public abstract class PlayerPropertyBooleanPlaceholder extends BooleanPlaceholder {

    public PlayerPropertyBooleanPlaceholder(boolean target) {
        super(target);
    }

    public PlayerPropertyBooleanPlaceholder(Map<String, Object> map) {
        super(map);
    }

    protected abstract Boolean getPropertyValue(Player p);

    @Override
    public Boolean computePlaceholder(Target target, Source source) {
        if (this.target) {
            if (target instanceof EntityTarget) {
                LivingEntity livingEntity = ((EntityTarget) target).getTarget();
                if (livingEntity instanceof Player) {
                    Player player = (Player) livingEntity;
                    return getPropertyValue(player);
                }
            }
        }
        LivingEntity livingEntity = (source.getCaster());
        if (livingEntity instanceof Player) {
            Player player = (Player) livingEntity;
            return getPropertyValue(player);
        }
        return false;
    }
}
