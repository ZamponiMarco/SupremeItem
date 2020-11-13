package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lLeft click skill", description = "gui.skill.left-click.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWYxMzNlOTE5MTlkYjBhY2VmZGMyNzJkNjdmZDg3YjRiZTg4ZGM0NGE5NTg5NTg4MjQ0NzRlMjFlMDZkNTNlNiJ9fX0=")
public class LeftClickSkill extends CooldownSkill {

    private static final int RAY_CAST_DISTANCE_DEFAULT = 30;

    private static final String CASTER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjY4YjQzMTE1MmU4MmFmNWRlZjg4ZjkxYmI2MWM2MjNiM2I3YWMzYWJlODJkMjc2ZmFkMzQ3Nzc2NDBmOTU5MCJ9fX0=";
    private static final String RAY_CAST_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc4N2I3YWZiNWE1OTk1Mzk3NWJiYTI0NzM3NDliNjAxZDU0ZDZmOTNjZWFjN2EwMmFjNjlhYWU3ZjliOCJ9fX0=";
    private static final String DISTANCE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFhNjg2MGQxMGQ3Yzg2ZmNjYzY1MjhkOWYyNTY0YTJmNTZkNWNmMzdlNzllZDVjNzc4NDk0MDI1MTVkNzc1MSJ9fX0=";

    @Serializable(headTexture = CASTER_HEAD, description = "gui.skill.left-click.caster-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    protected List<Action> onCasterActions;

    @Serializable(headTexture = RAY_CAST_HEAD, description = "gui.skill.left-click.ray-cast-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    protected List<Action> onRayCastPointActions;

    @Serializable(headTexture = DISTANCE_HEAD, description = "gui.skill.left-click.ray-cast-distance")
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "RAY_CAST_DISTANCE_DEFAULT")
    protected int onRayCastMaxDistance;

    public LeftClickSkill() {
        this(CONSUMABLE_DEFAULT, COOLDOWN_DEFAULT, COOLDOWN_MESSAGE_DEFAULT, Lists.newArrayList(), Lists.newArrayList(), RAY_CAST_DISTANCE_DEFAULT);
    }

    public LeftClickSkill(boolean consumable, int cooldown, boolean cooldownMessage, List<Action> onCasterActions, List<Action> onRayCastPointActions, int onRayCastMaxDistance) {
        super(consumable, cooldown, cooldownMessage);
        this.onCasterActions = onCasterActions;
        this.onRayCastPointActions = onRayCastPointActions;
        this.onRayCastMaxDistance = onRayCastMaxDistance;
    }

    public static LeftClickSkill deserialize(Map<String, Object> map) {
        boolean consumable = (boolean) map.getOrDefault("consumable", CONSUMABLE_DEFAULT);
        List<Action> onCasterActions = (List<Action>) map.getOrDefault("onCasterActions", Lists.newArrayList());
        int cooldown = (int) map.getOrDefault("cooldown", COOLDOWN_DEFAULT);
        List<Action> onRayCastPointActions = (List<Action>) map.getOrDefault("onRayCastPointActions", Lists.newArrayList());
        int onRayCastMaxDistance = (int) map.getOrDefault("onRayCastMaxDistance", RAY_CAST_DISTANCE_DEFAULT);
        boolean cooldownMessage = (boolean) map.getOrDefault("cooldownMessage", COOLDOWN_MESSAGE_DEFAULT);
        return new LeftClickSkill(consumable, cooldown, cooldownMessage, onCasterActions, onRayCastPointActions, onRayCastMaxDistance);
    }

    public SkillResult executeSkill(LivingEntity e, UUID id, ItemStack item) {
        return getSkillResult(id, item, e);
    }

    @Override
    protected boolean executeExactSkill(LivingEntity... e) {
        return executeCasterActions(e[0], onCasterActions) || executeRayCastActions(e[0], onRayCastMaxDistance, onRayCastPointActions);
    }

    @Override
    public String getName() {
        return "&cLeft click &6&lskill";
    }

    @Override
    public Set<SavedSkill> getUsedSavedSkills() {
        Set<SavedSkill> skills = new HashSet<>();
        SavedSkill.addSkillsFromActionsList(skills, onRayCastPointActions);
        SavedSkill.addSkillsFromActionsList(skills, onCasterActions);
        return skills;
    }
}
