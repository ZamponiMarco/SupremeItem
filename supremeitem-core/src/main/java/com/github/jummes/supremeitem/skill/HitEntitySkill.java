package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.placeholder.numeric.entity.NumericVariablePlaceholder;
import com.github.jummes.supremeitem.util.Utils;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lOn entity hit", description = "gui.skill.combat.hit-entity.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=")
public class HitEntitySkill extends CombatSkill {

    private static final NumericValue DEFAULT_DAMAGE = new NumericValue(new NumericVariablePlaceholder(false,
            "damage", false));

    private static final String DAMAGER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=";
    private static final String DAMAGED_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJlMmRkNTM3Y2I3NDM0YzM5MGQwNDgyZmE0NzI0N2NhM2ViNTZmMTlhOTNjMDRjNmM4NTgxMzUyYjhkOTUzOCJ9fX0=";

    @Serializable(headTexture = DAMAGER_HEAD, description = "gui.skill.combat.hit-entity.damager-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> onDamagerActions;
    @Serializable(headTexture = DAMAGED_HEAD, description = "gui.skill.combat.hit-entity.damaged-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> onDamagedActions;

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

    @Override
    public void executeSkill(UUID id, ItemStack item, Map<String, Object> args) {
        getSkillResult(id, item, args);
    }

    @Override
    protected void executeCooldownSkill(Map<String, Object> args) {
        LivingEntity damager = (LivingEntity) args.get("damager");
        LivingEntity damaged = (LivingEntity) args.get("damaged");

        damager.setMetadata("damage", new FixedMetadataValue(SupremeItem.getInstance(), args.get("damage")));
        damager.setMetadata("damageCause", new FixedMetadataValue(SupremeItem.getInstance(), args.
                get("damageCause")));

        onDamagedActions.forEach(action -> action.execute(new EntityTarget(damaged), new EntitySource(damager), args));
        onDamagerActions.forEach(action -> action.execute(new EntityTarget(damager), new EntitySource(damager), args));

        args.put("damage", Utils.getMetadata(damager.getMetadata("damage"), 0.0).asDouble());

        damager.removeMetadata("damage", SupremeItem.getInstance());
        damager.removeMetadata("damageCause", SupremeItem.getInstance());
    }

    @Override
    public String getName() {
        return "&cHit entity &6&lskill";
    }

    @Override
    public List<Action> getAbstractActions() {
        return Stream.concat(onDamagedActions.stream(), onDamagerActions.stream()).collect(Collectors.toList());
    }
}
