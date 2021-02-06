package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.Action;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Enumerable.Parent(classArray = {EntitySprintSkill.class, EntitySneakSkill.class, EntityJumpSkill.class})
@Enumerable.Displayable(name = "&c&lMovement Skills", description = "gui.skill.movement.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Y3YmMyYzFmNzdiM2Y5NTZjNTViZjU0NjY1YjdkYTVkOTYxMTFjY2M3NmY3NTdiN2I5ZmNkNTNlMjgxZjQifX19")
public abstract class MovementSkill extends CooldownSkill {

    public MovementSkill(boolean consumable, Set<Slot> allowedSlots, List<Action> onItemActions, CooldownOptions cooldownOptions) {
        super(consumable, allowedSlots, onItemActions, cooldownOptions);
    }

    public MovementSkill(Map<String, Object> map) {
        super(map);
    }
}
