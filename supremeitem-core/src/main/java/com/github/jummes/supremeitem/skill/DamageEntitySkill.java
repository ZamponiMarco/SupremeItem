package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.google.common.collect.Lists;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lOn entity damage", description = "gui.skill.damage-entity.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjUyNTU5ZjJiY2VhZDk4M2Y0YjY1NjFjMmI1ZjJiNTg4ZjBkNjExNmQ0NDY2NmNlZmYxMjAyMDc5ZDI3Y2E3NCJ9fX0=")
public class DamageEntitySkill extends CooldownSkill {

    private static final String DAMAGER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=";
    private static final String DAMAGED_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJlMmRkNTM3Y2I3NDM0YzM5MGQwNDgyZmE0NzI0N2NhM2ViNTZmMTlhOTNjMDRjNmM4NTgxMzUyYjhkOTUzOCJ9fX0=";

    @Serializable(headTexture = DAMAGED_HEAD, description = "gui.skill.damage-entity.damaged-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    protected List<Action> onDamagedActions;
    @Serializable(headTexture = DAMAGER_HEAD, description = "gui.skill.damage-entity.damager-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    protected List<Action> onDamagerActions;

    public DamageEntitySkill() {
        this(CONSUMABLE_DEFAULT, COOLDOWN_DEFAULT, COOLDOWN_MESSAGE_DEFAULT, Lists.newArrayList(), Lists.newArrayList());
    }

    public DamageEntitySkill(boolean consumable, int cooldown, boolean cooldownMessage, List<Action> onDamagedActions,
                             List<Action> onDamagerActions) {
        super(consumable, cooldown, cooldownMessage);
        this.onDamagedActions = onDamagedActions;
        this.onDamagerActions = onDamagerActions;
    }

    public static DamageEntitySkill deserialize(Map<String, Object> map) {
        boolean consumable = (boolean) map.getOrDefault("consumable", CONSUMABLE_DEFAULT);
        List<Action> onDamagedActions = (List<Action>) map.getOrDefault("onDamagedActions", Lists.newArrayList());
        List<Action> onDamagerActions = (List<Action>) map.getOrDefault("onDamagerActions", Lists.newArrayList());
        int cooldown = (int) map.getOrDefault("cooldown", COOLDOWN_DEFAULT);
        boolean cooldownMessage = (boolean) map.getOrDefault("cooldownMessage", COOLDOWN_MESSAGE_DEFAULT);
        return new DamageEntitySkill(consumable, cooldown, cooldownMessage, onDamagedActions, onDamagerActions);
    }

    public SkillResult executeSkill(LivingEntity damaged, LivingEntity damager, UUID id, ItemStack item) {
        return getSkillResult(id, item, damaged, damager);
    }

    @Override
    protected boolean executeExactSkill(LivingEntity... e) {
        return onDamagedActions.stream().anyMatch(action ->
                action.execute(new EntityTarget(e[0]), new EntitySource(e[0])).
                        equals(Action.ActionResult.CANCELLED)) ||
                onDamagerActions.stream().anyMatch(action ->
                        action.execute(new EntityTarget(e[1]), new EntitySource(e[0])).
                                equals(Action.ActionResult.CANCELLED));
    }

    @Override
    public String getName() {
        return "&cDamage Entity &6&lskill";
    }

    @Override
    public Set<SavedSkill> getUsedSavedSkills() {
        Set<SavedSkill> skills = new HashSet<>();
        SavedSkill.addSkillsFromActionsList(skills, onDamagedActions);
        SavedSkill.addSkillsFromActionsList(skills, onDamagerActions);
        return skills;
    }
}
