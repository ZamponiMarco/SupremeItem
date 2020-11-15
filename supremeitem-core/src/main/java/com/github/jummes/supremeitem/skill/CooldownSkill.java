package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.manager.CooldownManager;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public abstract class CooldownSkill extends Skill {

    protected static final int COOLDOWN_DEFAULT = 0;
    protected static final boolean COOLDOWN_MESSAGE_DEFAULT = true;

    protected static final String COOLDOWN_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";
    protected static final String COOLDOWN_MESSAGE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";

    @Serializable(headTexture = COOLDOWN_HEAD, description = "gui.skill.cooldown")
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "COOLDOWN_DEFAULT")
    protected int cooldown;

    @Serializable(headTexture = COOLDOWN_MESSAGE_HEAD, description = "gui.skill.cooldown-message")
    @Serializable.Optional(defaultValue = "COOLDOWN_MESSAGE_DEFAULT")
    protected boolean cooldownMessage;

    public CooldownSkill(boolean consumable, Set<EquipmentSlot> allowedSlots, int cooldown, boolean cooldownMessage) {
        super(consumable, allowedSlots);
        this.cooldown = cooldown;
        this.cooldownMessage = cooldownMessage;
    }

    public CooldownSkill(Map<String, Object> map) {
        super(map);
        this.cooldown = (int) map.getOrDefault("cooldown", COOLDOWN_DEFAULT);
        this.cooldownMessage = (boolean) map.getOrDefault("cooldownMessage", COOLDOWN_MESSAGE_DEFAULT);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("cooldown", cooldown);
        map.put("cooldownMessage", cooldownMessage);
        return map;
    }

    protected SkillResult getSkillResult(UUID id, ItemStack item, LivingEntity... e) {
        boolean cancelled = false;
        int currentCooldown = SupremeItem.getInstance().getCooldownManager().getCooldown(e[0], id, getClass());
        if (currentCooldown == 0) {
            consumeIfConsumable(id, item);
            cancelled = executeExactSkill(e);
            cooldown(e[0], id, cooldown, cooldownMessage);
        } else {
            if (e[0] instanceof Player) {
                SupremeItem.getInstance().getCooldownManager().switchCooldownContext((Player) e[0], id,
                        this.cooldown, getClass(), cooldownMessage);
            }
        }
        return cancelled ? SkillResult.CANCELLED : SkillResult.SUCCESS;
    }

    protected void cooldown(LivingEntity e, UUID id, int cooldown, boolean cooldownMessage) {
        if (cooldown > 0) {
            SupremeItem.getInstance().getCooldownManager().addCooldown(e,
                    new CooldownManager.CooldownInfo(id, cooldown, getClass()), cooldownMessage);
        }
    }

    protected abstract boolean executeExactSkill(LivingEntity... e);


    protected boolean executeCasterActions(LivingEntity e, List<Action> actions) {
        return actions.stream().anyMatch(action -> action.execute(new EntityTarget(e),
                new EntitySource(e)).equals(Action.ActionResult.CANCELLED));
    }

    protected boolean executeRayCastActions(LivingEntity e, int onRayCastMaxDistance, List<Action> onRayCastPointActions) {
        RayTraceResult result = e.rayTraceBlocks(onRayCastMaxDistance);
        if (result != null) {
            Location l = result.getHitPosition().toLocation(e.getWorld());
            return onRayCastPointActions.stream().anyMatch(action ->
                    action.execute(new LocationTarget(l),
                            new EntitySource(e)).equals(Action.ActionResult.CANCELLED));
        }
        return false;
    }
}
