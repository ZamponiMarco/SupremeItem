package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lOn entity shoot bow", description = "gui.skill.combat.bow-shoot.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzU0Nzk1MTA0MjJiMWM1ZGNjNzdmNzVmZGMzMzQ2ZWQ0ZDlkYmJjYzFlODg1YjRhMjk5MmEyNzM3MzM2NDZhOSJ9fX0=")
public class EntityBowShootSkill extends CombatSkill {

    @Serializable(headTexture = CASTER_HEAD, description = "gui.skill.caster-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> onEntityActions;

    public EntityBowShootSkill() {
        this(CONSUMABLE_DEFAULT, Sets.newHashSet(EquipmentSlot.values()), Lists.newArrayList(), new CooldownOptions(),
                Lists.newArrayList());
    }

    public EntityBowShootSkill(boolean consumable, Set<EquipmentSlot> allowedSlots, List<Action> onItemActions,
                               CooldownOptions cooldownOptions, List<Action> onEntityActions) {
        super(consumable, allowedSlots, onItemActions, cooldownOptions);
        this.onEntityActions = onEntityActions;
    }

    public EntityBowShootSkill(Map<String, Object> map) {
        super(map);
        this.onEntityActions = (List<Action>) map.getOrDefault("onEntityActions", Lists.newArrayList());
    }

    @Override
    public SkillResult executeSkill(UUID id, ItemStack item, Object... args) {
        LivingEntity e = (LivingEntity) args[0];
        return getSkillResult(id, item, e);
    }

    @Override
    public Map<String, Object> serialize() {
        Map map = super.serialize();
        map.put("onEntityActions", onEntityActions);
        return map;
    }

    @Override
    protected boolean executeExactSkill(LivingEntity... e) {
        return executeCasterActions(e[0], onEntityActions);
    }

    @Override
    public String getName() {
        return "&cShoot Bow &6&lskill";
    }

    @Override
    public List<Action> getAbstractActions() {
        return new ArrayList<>(onEntityActions);
    }
}
