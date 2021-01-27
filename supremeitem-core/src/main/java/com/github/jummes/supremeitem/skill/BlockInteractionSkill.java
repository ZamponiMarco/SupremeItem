package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BlockInteractionSkill extends InteractionSkill {

    private static final String BLOCK_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGMxNzU0ODUxZTM2N2U4YmViYTJhNmQ4ZjdjMmZlZGU4N2FlNzkzYWM1NDZiMGYyOTlkNjczMjE1YjI5MyJ9fX0=";

    @Serializable(headTexture = CASTER_HEAD, description = "gui.skill.caster-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> onEntityActions;

    @Serializable(headTexture = BLOCK_HEAD, description = "gui.skill.interact.block.block-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> onBlockActions;

    public BlockInteractionSkill() {
        this(CONSUMABLE_DEFAULT, Sets.newHashSet(EquipmentSlot.values()), Lists.newArrayList(), new CooldownOptions(),
                Lists.newArrayList(), Lists.newArrayList());
    }

    public BlockInteractionSkill(boolean consumable, Set<EquipmentSlot> allowedSlots, List<Action> onItemActions,
                                 CooldownOptions cooldownOptions, List<Action> onEntityActions, List<Action> onBlockActions) {
        super(consumable, allowedSlots, onItemActions, cooldownOptions);
        this.onEntityActions = onEntityActions;
        this.onBlockActions = onBlockActions;
    }

    public BlockInteractionSkill(Map<String, Object> map) {
        super(map);
        this.onEntityActions = (List<Action>) map.getOrDefault("onEntityActions", Lists.newArrayList());
        this.onBlockActions = (List<Action>) map.getOrDefault("onBlockActions", Lists.newArrayList());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("onEntityActions", onEntityActions);
        map.put("onBlockActions", onBlockActions);
        return map;
    }

    @Override
    protected boolean executeExactSkill(LivingEntity... e) {
        return executeCasterActions(e[0], onEntityActions);
    }

    private boolean exactBlockSkill(LivingEntity caster, Location loc) {
        return onBlockActions.stream().filter(action -> action.execute(new LocationTarget(loc),
                new EntitySource(caster)).equals(Action.ActionResult.CANCELLED)).count() > 0;
    }

    public SkillResult executeSkill(LivingEntity e, Location loc, UUID id, ItemStack item) {
        boolean cancelled = false;
        int currentCooldown = SupremeItem.getInstance().getCooldownManager().getCooldown(e, id, getClass());
        if (currentCooldown == 0) {
            consumeIfConsumable(id, item);
            cancelled = executeExactSkill(e) | executeItemActions(e, item) | exactBlockSkill(e, loc);
            cooldown(e, id);
        } else {
            if (e instanceof Player) {
                SupremeItem.getInstance().getCooldownManager().switchCooldownContext((Player) e, id,
                        getClass(), cooldownOptions);
            }
        }
        return cancelled ? SkillResult.CANCELLED : SkillResult.SUCCESS;
    }

    @Override
    public List<Action> getAbstractActions() {
        return Stream.concat(onEntityActions.stream(), onBlockActions.stream()).collect(Collectors.toList());
    }
}
