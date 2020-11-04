package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.manager.CooldownManager;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import java.util.List;
import java.util.UUID;

@Enumerable.Parent(classArray = {RightClickSkill.class, LeftClickSkill.class, HitEntitySkill.class, TimerSkill.class,
        DamageEntitySkill.class, EntitySneakSkill.class})
public abstract class Skill implements Model {

    protected static final List<Action> ACTIONS_DEFAULT = Lists.newArrayList();
    protected static final int COOLDOWN_DEFAULT = 0;
    protected static final boolean COOLDOWN_MESSAGE_DEFAULT = true;

    protected static final String COOLDOWN_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";
    protected static final String COOLDOWN_MESSAGE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";

    @Override

    public boolean equals(Object obj) {
        return getClass().equals(obj.getClass());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    protected void cooldown(LivingEntity e, UUID id, int cooldown, boolean cooldownMessage) {
        if (cooldown > 0) {
            SupremeItem.getInstance().getCooldownManager().addCooldown(e,
                    new CooldownManager.CooldownInfo(id, cooldown, getClass()), cooldownMessage);
        }
    }

    protected void consumeIfConsumable(UUID id, ItemStack item) {
        boolean consumable = SupremeItem.getInstance().getItemManager().getById(id).isConsumable();
        if (consumable) {
            int amount = item.getAmount();
            item.setAmount(--amount);
        }
    }

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

    public enum SkillResult {
        SUCCESS,
        CANCELLED,
        FAILURE
    }
}
