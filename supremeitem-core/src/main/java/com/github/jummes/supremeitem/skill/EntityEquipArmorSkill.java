package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
@Enumerable.Child
@Enumerable.Displayable(condition = "paperEnabled", name = "&c&lOn entity change armor", description = "gui.skill.combat.armor.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWNjZTgwMjQ2NzRiYTk0OGYzMWM0NjNjYmY5NzFiNDk0NmE1NGUzMjMxNDVmYWY0NTQ3YTYyNDJkY2Y2YTFjOCJ9fX0=")
public class EntityEquipArmorSkill extends CombatSkill {

    private static final String EQUIP_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWNjZTgwMjQ2NzRiYTk0OGYzMWM0NjNjYmY5NzFiNDk0NmE1NGUzMjMxNDVmYWY0NTQ3YTYyNDJkY2Y2YTFjOCJ9fX0=";

    @Serializable(headTexture = CASTER_HEAD, description = "gui.skill.caster-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> onEntityActions;
    @Serializable(headTexture = EQUIP_HEAD, description = "gui.skill.combat.armor.equip")
    private boolean onEquip;

    public EntityEquipArmorSkill() {
        this(CONSUMABLE_DEFAULT, Sets.newHashSet(EquipmentSlot.values()), Lists.newArrayList(), new CooldownOptions(),
                Lists.newArrayList(), true);
    }

    public EntityEquipArmorSkill(boolean consumable, Set<EquipmentSlot> allowedSlots, List<Action> onItemActions,
                                 CooldownOptions cooldownOptions, List<Action> onEntityActions, boolean onEquip) {
        super(consumable, allowedSlots, onItemActions, cooldownOptions);
        this.onEntityActions = onEntityActions;
        this.onEquip = onEquip;
    }

    public EntityEquipArmorSkill(Map<String, Object> map) {
        super(map);
        this.onEntityActions = (List<Action>) map.getOrDefault("onEntityActions", Lists.newArrayList());
        this.onEquip = (boolean) map.getOrDefault("onEquip", true);
    }

    public static boolean paperEnabled(ModelPath path) {
        return SupremeItem.getInstance().getPaperHook().isEquipChangeEnabled();
    }

    @Override
    public void executeSkill(UUID id, ItemStack item, Map<String, Object> args) {
        getSkillResult(id, item, args);
    }

    @Override
    public Map<String, Object> serialize() {
        Map map = super.serialize();
        map.put("onEntityActions", onEntityActions);
        map.put("onEquip", onEquip);
        return map;
    }

    @Override
    protected void executeCooldownSkill(Map<String, Object> args) {
        executeCasterActions(onEntityActions, args);
    }

    @Override
    public String getName() {
        return "&cEquip item &6&lskill";
    }

    @Override
    public List<Action> getAbstractActions() {
        return new ArrayList<>(onEntityActions);
    }
}
