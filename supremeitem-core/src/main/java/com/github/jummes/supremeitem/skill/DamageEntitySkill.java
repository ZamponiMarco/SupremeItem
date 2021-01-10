package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lOn entity damage", description = "gui.skill.combat.damage-entity.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjUyNTU5ZjJiY2VhZDk4M2Y0YjY1NjFjMmI1ZjJiNTg4ZjBkNjExNmQ0NDY2NmNlZmYxMjAyMDc5ZDI3Y2E3NCJ9fX0=")
public class DamageEntitySkill extends CombatSkill {

    private static final String DAMAGER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=";
    private static final String DAMAGED_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJlMmRkNTM3Y2I3NDM0YzM5MGQwNDgyZmE0NzI0N2NhM2ViNTZmMTlhOTNjMDRjNmM4NTgxMzUyYjhkOTUzOCJ9fX0=";

    @Serializable(headTexture = DAMAGED_HEAD, description = "gui.skill.combat.damage-entity.damaged-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    protected List<Action> onDamagedActions;
    @Serializable(headTexture = DAMAGER_HEAD, description = "gui.skill.combat.damage-entity.damager-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    protected List<Action> onDamagerActions;

    public DamageEntitySkill() {
        this(CONSUMABLE_DEFAULT, Sets.newHashSet(EquipmentSlot.values()), Lists.newArrayList(), new CooldownOptions(),
                Lists.newArrayList(), Lists.newArrayList());
    }

    public DamageEntitySkill(boolean consumable, Set<EquipmentSlot> allowedSlots, List<Action> onItemActions,
                             CooldownOptions cooldownOptions, List<Action> onDamagedActions, List<Action> onDamagerActions) {
        super(consumable, allowedSlots, onItemActions, cooldownOptions);
        this.onDamagedActions = onDamagedActions;
        this.onDamagerActions = onDamagerActions;
    }

    public DamageEntitySkill(Map<String, Object> map) {
        super(map);
        this.onDamagedActions = (List<Action>) map.getOrDefault("onDamagedActions", Lists.newArrayList());
        this.onDamagerActions = (List<Action>) map.getOrDefault("onDamagerActions", Lists.newArrayList());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("onDamagedActions", onDamagedActions);
        map.put("onDamagerActions", onDamagerActions);
        return map;
    }

    public SkillResult executeSkill(LivingEntity damaged, LivingEntity damager, UUID id, ItemStack item) {
        return getSkillResult(id, item, damaged, damager);
    }

    @Override
    protected boolean executeExactSkill(LivingEntity... e) {
        return onDamagedActions.stream().filter(action ->
                action.execute(new EntityTarget(e[0]), new EntitySource(e[0])).
                        equals(Action.ActionResult.CANCELLED)).count() > 0 ||
                onDamagerActions.stream().filter(action ->
                        action.execute(new EntityTarget(e[1]), new EntitySource(e[0])).
                                equals(Action.ActionResult.CANCELLED)).count() > 0;
    }

    @Override
    public String getName() {
        return "&cDamage Entity &6&lskill";
    }

    @Override
    public List<Action> getAbstractActions() {
        return Stream.concat(onDamagedActions.stream(), onDamagerActions.stream()).collect(Collectors.toList());
    }

}
