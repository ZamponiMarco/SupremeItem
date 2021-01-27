package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lBlock Break Skill", description = "gui.skill.interact.block.break.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZlYTIxMzU4Mzg0NjE1MzQzNzJmMmRhNmM4NjJkMjFjZDVmM2QyYzcxMTlmMmJiNjc0YmJkNDI3OTEifX19")
public class BlockBreakSkill extends BlockInteractionSkill {

    public BlockBreakSkill() {
        this(CONSUMABLE_DEFAULT, Sets.newHashSet(EquipmentSlot.values()), Lists.newArrayList(), new CooldownOptions(),
                Lists.newArrayList(), Lists.newArrayList());
    }

    public BlockBreakSkill(boolean consumable, Set<EquipmentSlot> allowedSlots, List<Action> onItemActions,
                           CooldownOptions cooldownOptions, List<Action> onEntityActions, List<Action> onBlockActions) {
        super(consumable, allowedSlots, onItemActions, cooldownOptions, onEntityActions, onBlockActions);
    }

    public BlockBreakSkill(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String getName() {
        return "&cBlock Break &6&lskill";
    }
}