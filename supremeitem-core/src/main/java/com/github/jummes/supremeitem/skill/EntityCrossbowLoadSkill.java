package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.Lists;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@Enumerable.Child
@Enumerable.Displayable(condition = "paperEnabled", name = "&c&lOn entity crossbow load", description = "gui.skill.combat.crossbow-load.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFlZTZiYjM3Y2JmYzkyYjBkODZkYjVhZGE0NzkwYzY0ZmY0NDY4ZDY4Yjg0OTQyZmRlMDQ0MDVlOGVmNTMzMyJ9fX0=")
public class EntityCrossbowLoadSkill extends CombatSkill {

    @Serializable(headTexture = CASTER_HEAD, description = "gui.skill.caster-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> onEntityActions;

    public EntityCrossbowLoadSkill() {
        this(CONSUMABLE_DEFAULT, DEFAULT_SLOTS.stream().map(Slot::clone).collect(Collectors.toSet()), Lists.newArrayList(),
                new CooldownOptions(), Lists.newArrayList());
    }

    public EntityCrossbowLoadSkill(boolean consumable, Set<Slot> allowedSlots, List<Action> onItemActions,
                                   CooldownOptions cooldownOptions, List<Action> onEntityActions) {
        super(consumable, allowedSlots, onItemActions, cooldownOptions);
        this.onEntityActions = onEntityActions;
    }

    public EntityCrossbowLoadSkill(Map<String, Object> map) {
        super(map);
        this.onEntityActions = (List<Action>) map.getOrDefault("onEntityActions", Lists.newArrayList());
    }

    public static boolean paperEnabled(ModelPath path) {
        return SupremeItem.getInstance().getPaperHook().isCrossbowLoadEventEnabled();
    }

    @Override
    public void executeSkill(UUID id, ItemStack item, Map<String, Object> args) {
        getSkillResult(id, item, args);
    }

    @Override
    public Map<String, Object> serialize() {
        Map map = super.serialize();
        map.put("onEntityActions", onEntityActions);
        return map;
    }

    @Override
    protected void executeCooldownSkill(Map<String, Object> args) {
        executeCasterActions(onEntityActions, args);
    }

    @Override
    public String getName() {
        return "&cCrossbow load &6&lskill";
    }

    @Override
    public List<Action> getAbstractActions() {
        return new ArrayList<>(onEntityActions);
    }
}
