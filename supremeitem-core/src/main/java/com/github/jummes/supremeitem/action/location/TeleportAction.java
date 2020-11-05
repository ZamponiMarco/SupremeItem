package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lTeleport", description = "gui.action.teleport.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWNiN2MyMWNjNDNkYzE3Njc4ZWU2ZjE2NTkxZmZhYWIxZjYzN2MzN2Y0ZjZiYmQ4Y2VhNDk3NDUxZDc2ZGI2ZCJ9fX0=")
public class TeleportAction extends LocationAction {

    public TeleportAction(boolean target) {
        super(target);
    }

    public TeleportAction() {
        this(TARGET_DEFAULT);
    }

    public static TeleportAction deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new TeleportAction(target);
    }

    @Override
    public ActionResult execute(Target target, Source source) {

        LivingEntity entity = getEntity(target, source);

        if (entity == null) {
            return ActionResult.FAILURE;
        }

        Location location = getLocation(target, source).clone();

        entity.teleport(location);

        return ActionResult.SUCCESS;
    }

    public LivingEntity getEntity(Target target, Source source) {
        if (this.target) {
            return source.getCaster();
        } else if (target instanceof EntityTarget) {
            return ((EntityTarget) target).getTarget();
        }
        return null;
    }

    @Override
    public Action clone() {
        return new TeleportAction(target);
    }

    @Override
    public String getName() {
        return "&6&lTeleport";
    }
}
