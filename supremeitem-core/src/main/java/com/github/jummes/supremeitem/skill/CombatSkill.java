package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.Action;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Enumerable.Displayable(name = "&c&lCombat Skills", description = "gui.skill.combat.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDliNDdhZTk5M2FkM2RlZWExZTFiZmM0Yzk2ZjVlYzgwZDJjMDYxNjNlYzczMjhmMzAxMGYxMzgzMDlmNGIifX19")
@Enumerable.Parent(classArray = {DamageEntitySkill.class, HitEntitySkill.class, EntityBowShootSkill.class})
public abstract class CombatSkill extends CooldownSkill {
    public CombatSkill(boolean consumable, Set<EquipmentSlot> allowedSlots, List<Action> onItemActions, CooldownOptions cooldownOptions) {
        super(consumable, allowedSlots, onItemActions, cooldownOptions);
    }

    public CombatSkill(Map<String, Object> map) {
        super(map);
    }
}
