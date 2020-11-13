package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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
        this(CONSUMABLE_DEFAULT, COOLDOWN_DEFAULT, COOLDOWN_MESSAGE_DEFAULT, Lists.newArrayList(), Lists.newArrayList());
    }

    public HitEntitySkill(boolean consumable, int cooldown, boolean cooldownMessage, List<Action> onDamagerActions, List<Action> onDamagedActions) {
        super(consumable, cooldown, cooldownMessage);
        this.onDamagerActions = onDamagerActions;
        this.onDamagedActions = onDamagedActions;
    }

    public static HitEntitySkill deserialize(Map<String, Object> map) {
        boolean consumable = (boolean) map.getOrDefault("consumable", CONSUMABLE_DEFAULT);
        List<Action> onDamagerActions = (List<Action>) map.getOrDefault("onDamagerActions", Lists.newArrayList());
        List<Action> onDamagedActions = (List<Action>) map.getOrDefault("onDamagedActions", Lists.newArrayList());
        int cooldown = (int) map.getOrDefault("cooldown", COOLDOWN_DEFAULT);
        boolean cooldownMessage = (boolean) map.getOrDefault("cooldownMessage", COOLDOWN_MESSAGE_DEFAULT);
        return new HitEntitySkill(consumable, cooldown, cooldownMessage, onDamagerActions, onDamagedActions);
    }

    public SkillResult executeSkill(LivingEntity damager, LivingEntity damaged, UUID id, ItemStack item) {
        return getSkillResult(id, item, damager, damaged);
    }

    @Override
    protected boolean executeExactSkill(LivingEntity... e) {
        return onDamagedActions.stream().anyMatch(action ->
                action.execute(new EntityTarget(e[1]), new EntitySource(e[0])).
                        equals(Action.ActionResult.CANCELLED)) ||
                onDamagerActions.stream().anyMatch(action ->
                        action.execute(new EntityTarget(e[0]), new EntitySource(e[0])).
                                equals(Action.ActionResult.CANCELLED));
    }

    @Override
    public String getName() {
        return "&cHit entity &6&lskill";
    }

    @Override
    public Set<SavedSkill> getUsedSavedSkills() {
        Set<SavedSkill> skills = new HashSet<>();
        SavedSkill.addSkillsFromActionsList(skills, onDamagedActions);
        SavedSkill.addSkillsFromActionsList(skills, onDamagerActions);
        return skills;
    }
}
