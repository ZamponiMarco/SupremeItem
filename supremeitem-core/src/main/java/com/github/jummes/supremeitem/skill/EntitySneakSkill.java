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
@Enumerable.Displayable(name = "&c&lOn Entity Sneak", description = "gui.skill.entity-sneak.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTQyMzI5YTljNDEwNDA4NDE5N2JkNjg4NjE1ODUzOTg0ZDM3ZTE3YzJkZDIzZTNlNDEyZGQ0MmQ3OGI5OGViIn19fQ")
public class EntitySneakSkill extends CooldownSkill {

    private static final String CASTER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjY4YjQzMTE1MmU4MmFmNWRlZjg4ZjkxYmI2MWM2MjNiM2I3YWMzYWJlODJkMjc2ZmFkMzQ3Nzc2NDBmOTU5MCJ9fX0=";

    @Serializable(headTexture = CASTER_HEAD, description = "gui.skill.left-click.caster-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> onEntityActions;

    public EntitySneakSkill() {
        this(CONSUMABLE_DEFAULT, COOLDOWN_DEFAULT, COOLDOWN_MESSAGE_DEFAULT, Lists.newArrayList());
    }

    public EntitySneakSkill(boolean consumable, int cooldown, boolean cooldownMessage, List<Action> onEntityActions) {
        super(consumable, cooldown, cooldownMessage);
        this.onEntityActions = onEntityActions;
    }

    public static EntitySneakSkill deserialize(Map<String, Object> map) {
        boolean consumable = (boolean) map.getOrDefault("consumable", CONSUMABLE_DEFAULT);
        List<Action> onEntityActions = (List<Action>) map.getOrDefault("onEntityActions", Lists.newArrayList());
        int cooldown = (int) map.getOrDefault("cooldown", COOLDOWN_DEFAULT);
        boolean cooldownMessage = (boolean) map.getOrDefault("cooldownMessage", COOLDOWN_MESSAGE_DEFAULT);
        return new EntitySneakSkill(consumable, cooldown, cooldownMessage, onEntityActions);
    }

    public SkillResult executeSkill(LivingEntity e, UUID id, ItemStack item) {
        return getSkillResult(id, item, e);
    }

    @Override
    protected boolean executeExactSkill(LivingEntity... e) {
        return executeCasterActions(e[0], onEntityActions);
    }

    @Override
    public String getName() {
        return "&cEntity Sneak &6&lskill";
    }

    @Override
    public Set<SavedSkill> getUsedSavedSkills() {
        Set<SavedSkill> skills = new HashSet<>();
        SavedSkill.addSkillsFromActionsList(skills, onEntityActions);
        return skills;
    }
}
