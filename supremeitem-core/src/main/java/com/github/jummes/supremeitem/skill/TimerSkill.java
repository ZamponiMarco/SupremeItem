package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lTimer skill", description = "gui.skill.timer.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=")
public class TimerSkill extends Skill {

    private static final int TIMER_DEFAULT = 20;

    private static final String WEARER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjY4YjQzMTE1MmU4MmFmNWRlZjg4ZjkxYmI2MWM2MjNiM2I3YWMzYWJlODJkMjc2ZmFkMzQ3Nzc2NDBmOTU5MCJ9fX0=";
    private static final String COOLDOWN_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";

    @Serializable(headTexture = WEARER_HEAD, description = "gui.skill.timer.wearer-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> onWearerActions;

    @Serializable(headTexture = COOLDOWN_HEAD, description = "gui.skill.timer.timer")
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "TIMER_DEFAULT")
    private int timer;


    public TimerSkill() {
        this(CONSUMABLE_DEFAULT, Sets.newHashSet(EquipmentSlot.values()), Lists.newArrayList(), Lists.newArrayList(), TIMER_DEFAULT);
    }

    public TimerSkill(boolean consumable, Set<EquipmentSlot> allowedSlots, List<Action> onItemActions,
                      List<Action> onWearerActions, int timer) {
        super(consumable, allowedSlots, onItemActions);
        this.onWearerActions = onWearerActions;
        this.timer = timer;
    }

    public TimerSkill(Map<String, Object> map) {
        super(map);
        this.onWearerActions = (List<Action>) map.getOrDefault("onWearerActions", Lists.newArrayList());
        this.timer = (int) map.getOrDefault("timer", TIMER_DEFAULT);
    }


    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("onWearerActions", onWearerActions);
        map.put("timer", timer);
        return map;
    }

    @Override
    public String getName() {
        return "&cTimer &6&lskill";
    }

    @Override
    public void changeSkillName(String oldName, String newName) {
        onWearerActions.forEach(action -> action.changeSkillName(oldName, newName));
    }
}
