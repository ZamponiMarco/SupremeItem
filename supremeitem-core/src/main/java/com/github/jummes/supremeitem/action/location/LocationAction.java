package com.github.jummes.supremeitem.action.location;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.Action;

@Enumerable.Parent(classArray = {ParticleAction.class, SoundAction.class})
@Enumerable.Displayable(name = "&9&lAction &6» &cLocation Targetable", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ5YjE0NjU2MTljNDJjN2NiMDZjZTFkMmNlNmViODRhZGQ2ZmM5YjIxYTE2ZGRhMjNmYWQyNDgwZTExZmYyIn19fQ==")
public abstract class LocationAction extends Action {
}
