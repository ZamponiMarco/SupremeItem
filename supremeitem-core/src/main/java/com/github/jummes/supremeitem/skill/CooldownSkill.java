package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.ItemTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.cooldown.CooldownInfo;
import com.github.jummes.supremeitem.cooldown.bar.ActionBar;
import com.github.jummes.supremeitem.cooldown.bar.CooldownBar;
import com.github.jummes.supremeitem.cooldown.bar.NoBar;
import lombok.Getter;
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

    protected static final String COOLDOWN_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";

    @Serializable(headTexture = COOLDOWN_HEAD, description = "gui.skill.cooldown-options")
    protected CooldownOptions cooldownOptions;

    public CooldownSkill(boolean consumable, Set<EquipmentSlot> allowedSlots, List<Action> onItemActions, CooldownOptions cooldownOptions) {
        super(consumable, allowedSlots, onItemActions);
        this.cooldownOptions = cooldownOptions;
    }

    public CooldownSkill(Map<String, Object> map) {
        super(map);
        this.cooldownOptions = (CooldownOptions) map.getOrDefault("cooldownOptions", new CooldownOptions());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("cooldownOptions", cooldownOptions);
        return map;
    }

    protected void getSkillResult(UUID itemId, ItemStack item, Map<String, Object> args) {
        LivingEntity caster = (LivingEntity) args.get("caster");
        int currentCooldown = SupremeItem.getInstance().getCooldownManager().getCooldown(caster, itemId, this.id);
        if (currentCooldown == 0) {
            consumeIfConsumable(itemId, item);
            executeCooldownSkill(args);
            executeItemActions(caster, item, args);
            cooldown(caster, itemId);
        } else {
            if (caster instanceof Player) {
                cooldownOptions.getBar().switchCooldownContext((Player) caster, itemId,
                        this.id, cooldownOptions.getCooldown());
            }
        }
    }

    protected void cooldown(LivingEntity e, UUID itemId) {
        if (cooldownOptions.cooldown > 0) {
            SupremeItem.getInstance().getCooldownManager().addCooldown(e,
                    new CooldownInfo(itemId, cooldownOptions.cooldown, this.id), cooldownOptions);
        }
    }

    protected abstract void executeCooldownSkill(Map<String, Object> args);

    protected void executeItemActions(LivingEntity e, ItemStack item, Map<String, Object> map) {
        onItemActions.forEach(action -> action.execute(new ItemTarget(item, e),
                new EntitySource(e), map));
    }

    protected void executeCasterActions(List<Action> actions, Map<String, Object> map) {
        LivingEntity e = (LivingEntity) map.get("caster");
        actions.forEach(action -> action.execute(new EntityTarget(e), new EntitySource(e), map));
    }

    protected void executeRayCastActions(int onRayCastMaxDistance,
                                         List<Action> onRayCastPointActions, Map<String, Object> map) {
        LivingEntity e = (LivingEntity) map.get("caster");
        RayTraceResult result = e.rayTraceBlocks(onRayCastMaxDistance);
        if (result != null) {
            Location l = result.getHitPosition().toLocation(e.getWorld());
            onRayCastPointActions.forEach(action -> action.execute(new LocationTarget(l), new EntitySource(e), map));
        }
    }

    @Getter
    public static class CooldownOptions implements Model {
        protected static final int COOLDOWN_DEFAULT = 0;
        protected static final String COOLDOWN_MESSAGE_FORMAT_DEFAULT = "&2Cooldown &6[%bar&6]";
        protected static final String COOLDOWN_MESSAGE_BAR_DEFAULT = "|";
        protected static final int COOLDOWN_MESSAGE_BAR_COUNT_DEFAULT = 30;


        protected static final String COOLDOWN_MESSAGE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";
        private static final String BAR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFmMGM5NWNlYmEzNGM3ZmU2ZDMzNDA0ZmViODdiNDE4NGNjY2UxNDM5Nzg2MjJjMTY0N2ZlYWVkMmI2MzI3NCJ9fX0==";

        @Serializable(headTexture = COOLDOWN_HEAD, description = "gui.skill.cooldown.cooldown")
        @Serializable.Number(minValue = 0, scale = 1)
        @Serializable.Optional(defaultValue = "COOLDOWN_DEFAULT")
        protected int cooldown;

        @Serializable(headTexture = BAR_HEAD, description = "gui.skill.cooldown.bar.description",
                additionalDescription = {"gui.additional-tooltips.recreate"})
        protected CooldownBar bar;

        public CooldownOptions() {
            this(COOLDOWN_DEFAULT, new ActionBar());
        }

        public CooldownOptions(int cooldown, CooldownBar bar) {
            this.cooldown = cooldown;
            this.bar = bar;
        }

        public CooldownOptions(Map<String, Object> map) {
            this.cooldown = (int) map.getOrDefault("cooldown", COOLDOWN_DEFAULT);
            boolean cooldownMessage;
            try {
                cooldownMessage = (boolean) map.get("cooldownMessage");
            } catch (NullPointerException e) {
                cooldownMessage = false;
            }
            if (cooldownMessage) {
                String cooldownMessageFormat = (String) map.getOrDefault("cooldownMessageFormat",
                        COOLDOWN_MESSAGE_FORMAT_DEFAULT);
                String cooldownMessageBar = (String) map.getOrDefault("cooldownMessageBar", COOLDOWN_MESSAGE_BAR_DEFAULT);
                int cooldownMessageBarCount = (int) map.getOrDefault("cooldownMessageBarCount",
                        COOLDOWN_MESSAGE_BAR_COUNT_DEFAULT);
                this.bar = new ActionBar();
            } else {
                this.bar = (CooldownBar) map.getOrDefault("bar", new NoBar());
            }
        }
    }
}
