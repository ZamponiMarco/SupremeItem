package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lOn entity hit", description = "gui.skill.hit-entity.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=")
public class HitEntitySkill extends CooldownSkill {

    private static final String DAMAGER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=";
    private static final String DAMAGED_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJlMmRkNTM3Y2I3NDM0YzM5MGQwNDgyZmE0NzI0N2NhM2ViNTZmMTlhOTNjMDRjNmM4NTgxMzUyYjhkOTUzOCJ9fX0=";

    @Serializable(headTexture = DAMAGER_HEAD, description = "gui.skill.hit-entity.damager-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    protected List<Action> onDamagerActions;
    @Serializable(headTexture = DAMAGED_HEAD, description = "gui.skill.hit-entity.damaged-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    protected List<Action> onDamagedActions;

    public HitEntitySkill() {
        this(CONSUMABLE_DEFAULT, Sets.newHashSet(EquipmentSlot.values()), Lists.newArrayList(), new CooldownOptions(),
                Lists.newArrayList(), Lists.newArrayList());
    }

    public HitEntitySkill(boolean consumable, Set<EquipmentSlot> allowedSlots, List<Action> onItemActions,
                          CooldownOptions cooldownOptions, List<Action> onDamagerActions, List<Action> onDamagedActions) {
        super(consumable, allowedSlots, onItemActions, cooldownOptions);
        this.onDamagerActions = onDamagerActions;
        this.onDamagedActions = onDamagedActions;
    }

    public HitEntitySkill(Map<String, Object> map) {
        super(map);
        this.onDamagerActions = (List<Action>) map.getOrDefault("onDamagerActions", Lists.newArrayList());
        this.onDamagedActions = (List<Action>) map.getOrDefault("onDamagedActions", Lists.newArrayList());
    }


    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("onDamagerActions", onDamagerActions);
        map.put("onDamagedActions", onDamagedActions);
        return map;
    }

    public SkillResult executeSkill(LivingEntity damager, LivingEntity damaged, UUID id, ItemStack item) {
        return getSkillResult(id, item, damager, damaged);
    }

    @Override
    protected boolean executeExactSkill(LivingEntity... e) {
        return onDamagedActions.stream().filter(action ->
                action.execute(new EntityTarget(e[1]), new EntitySource(e[0])).
                        equals(Action.ActionResult.CANCELLED)).count() > 0 ||
                onDamagerActions.stream().filter(action ->
                        action.execute(new EntityTarget(e[0]), new EntitySource(e[0])).
                                equals(Action.ActionResult.CANCELLED)).count() > 0;
    }

    @Override
    public String getName() {
        return "&cHit entity &6&lskill";
    }

    @Override
    public void changeSkillName(String oldName, String newName) {
        onDamagedActions.forEach(action -> action.changeSkillName(oldName, newName));
        onDamagerActions.forEach(action -> action.changeSkillName(oldName, newName));
    }
}
