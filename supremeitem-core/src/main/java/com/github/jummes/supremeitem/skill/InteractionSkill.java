package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.Action;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Enumerable.Parent(classArray = {LeftClickSkill.class, RightClickSkill.class, BlockInteractionSkill.class,
        BlockBreakSkill.class, BlockPlaceSkill.class})
@Enumerable.Displayable(name = "&c&lInteraction Skills", description = "gui.skill.interact.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThlZDg2M2QxNDA1ZGQ4YWM0OGU4ZTU3MTlhYWRmYWRiYTM5Y2RjNjllZTY3MzM2NTU4ZmE4MTYwZTQ3NTk0OCJ9fX0=")
public abstract class InteractionSkill extends CooldownSkill {
    public InteractionSkill(boolean consumable, Set<Slot> allowedSlots, List<Action> onItemActions, CooldownOptions cooldownOptions) {
        super(consumable, allowedSlots, onItemActions, cooldownOptions);
    }

    public InteractionSkill(Map<String, Object> map) {
        super(map);
    }
}
