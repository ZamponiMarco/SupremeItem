package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lBlock Place Skill", description = "gui.skill.interact.block.place.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGMxNzU0ODUxZTM2N2U4YmViYTJhNmQ4ZjdjMmZlZGU4N2FlNzkzYWM1NDZiMGYyOTlkNjczMjE1YjI5MyJ9fX0=")
public class BlockPlaceSkill extends BlockInteractionSkill {

    public BlockPlaceSkill() {
        this(CONSUMABLE_DEFAULT, DEFAULT_SLOTS.stream().map(Slot::clone).collect(Collectors.toSet()), Lists.newArrayList(),
                new CooldownOptions(), Lists.newArrayList(), Lists.newArrayList());
    }

    public BlockPlaceSkill(boolean consumable, Set<Slot> allowedSlots, List<Action> onItemActions,
                           CooldownOptions cooldownOptions, List<Action> onEntityActions, List<Action> onBlockActions) {
        super(consumable, allowedSlots, onItemActions, cooldownOptions, onEntityActions, onBlockActions);
    }

    public BlockPlaceSkill(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String getName() {
        return "&cBlock Place &6&lskill";
    }
}
