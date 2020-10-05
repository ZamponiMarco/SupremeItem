package com.github.jummes.supremeitem.placeholder.bool;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lOn Ground Placeholder", description = "gui.placeholder.on-ground.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzk1ZDM3OTkzZTU5NDA4MjY3ODQ3MmJmOWQ4NjgyMzQxM2MyNTBkNDMzMmEyYzdkOGM1MmRlNDk3NmIzNjIifX19")
public class OnGroundPlaceholder extends BooleanPlaceholder {

    public OnGroundPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public OnGroundPlaceholder(boolean target) {
        super(target);
    }

    public static OnGroundPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new OnGroundPlaceholder(target);
    }

    @Override
    public Boolean computePlaceholder(Target target, Source source) {
        if (this.target) {
            if (target instanceof EntityTarget) {
                return ((EntityTarget) target).getTarget().isOnGround();
            }
        }
        if (source instanceof EntitySource) {
            return ((EntitySource) source).getSource().isOnGround();
        }
        return false;
    }
}
