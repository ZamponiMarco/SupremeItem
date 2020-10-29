package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.Location;

@Enumerable.Parent(classArray = {ParticleAction.class, SoundAction.class, SetBlockAction.class, MoveLocationTargetAction.class,
        TeleportAction.class})
@Enumerable.Displayable(name = "&9&lAction &6» &cLocation Targetable", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ5YjE0NjU2MTljNDJjN2NiMDZjZTFkMmNlNmViODRhZGQ2ZmM5YjIxYTE2ZGRhMjNmYWQyNDgwZTExZmYyIn19fQ==")
public abstract class LocationAction extends Action {
    public LocationAction(boolean target) {
        super(target);
    }

    protected Location getLocation(Target target, Source source) {
        return getLocation(target, source, false);
    }

    protected Location getLocation(Target target, Source source, boolean eyes) {
        if (this.target) {
            if (eyes && target instanceof EntityTarget) {
                return ((EntityTarget) target).getTarget().getEyeLocation();
            }
            return target.getLocation();
        }
        if (eyes && source instanceof EntitySource) {
            return source.getCaster().getEyeLocation();
        }
        return source.getLocation();
    }
}
