package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lOn Entity Sneak", description = "gui.skill.movement.entity-sneak.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTQyMzI5YTljNDEwNDA4NDE5N2JkNjg4NjE1ODUzOTg0ZDM3ZTE3YzJkZDIzZTNlNDEyZGQ0MmQ3OGI5OGViIn19fQ")
public class EntitySneakSkill extends MovementSkill {

    private static final boolean ACTIVATE_DEFAULT = true;

    private static final String ACTIVATION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmE4OTc4Y2NiZjU3NmY0NDZlMjFjNTFkM2U4MGZjN2Y4NTY2ZWI3MjY1Y2M0M2M0YWQ3MWNmYjc4YzE2NTI1NyJ9fX0=";

    @Serializable(headTexture = CASTER_HEAD, description = "gui.skill.caster-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> onEntityActions;
    @EqualsAndHashCode.Include
    @Serializable(headTexture = ACTIVATION_HEAD, description = "gui.skill.activate")
    private boolean onActivate;

    public EntitySneakSkill() {
        this(CONSUMABLE_DEFAULT, Sets.newHashSet(EquipmentSlot.values()), Lists.newArrayList(), new CooldownOptions(),
                Lists.newArrayList(), true);
    }

    public EntitySneakSkill(boolean consumable, Set<EquipmentSlot> allowedSlots, List<Action> onItemActions,
                            CooldownOptions cooldownOptions, List<Action> onEntityActions, boolean onActivate) {
        super(consumable, allowedSlots, onItemActions, cooldownOptions);
        this.onEntityActions = onEntityActions;
        this.onActivate = onActivate;
    }

    public EntitySneakSkill(Map<String, Object> map) {
        super(map);
        onEntityActions = (List<Action>) map.getOrDefault("onEntityActions", Lists.newArrayList());
        boolean old = (boolean) map.getOrDefault("onActivateSneak", ACTIVATE_DEFAULT);
        onActivate = (boolean) map.getOrDefault("onActivate", old);
    }

    @Override
    public void executeSkill(UUID id, ItemStack item, Map<String, Object> args) {
        getSkillResult(id, item, args);
    }


    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("onEntityActions", onEntityActions);
        map.put("onActivate", onActivate);
        return map;
    }

    @Override
    protected void executeCooldownSkill(Map<String, Object> args) {
        executeCasterActions(onEntityActions, args);
    }

    @Override
    public String getName() {
        return String.format("&cEntity %s Sneak &6&lskill", onActivate ? "Activate" : "Deactivate");
    }

    @Override
    public List<Action> getAbstractActions() {
        return new ArrayList<>(onEntityActions);
    }

}
