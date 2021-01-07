package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.manager.CooldownManager;
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

    @Serializable(headTexture = COOLDOWN_HEAD)
    protected CooldownOptions cooldownOptions;

    public CooldownSkill(boolean consumable, Set<EquipmentSlot> allowedSlots, CooldownOptions cooldownOptions) {
        super(consumable, allowedSlots);
        this.cooldownOptions = cooldownOptions;
    }

    public CooldownSkill(Map<String, Object> map) {
        super(map);
        try {
            int cooldown = (int) map.get("cooldown");
            boolean cooldownMessage = (boolean) map.get("cooldownMessage");
            this.cooldownOptions = new CooldownOptions(cooldown, cooldownMessage, "&2Cooldown &6[%s&6]",
                    "|", 30);
        } catch (Exception e) {
            this.cooldownOptions = (CooldownOptions) map.getOrDefault("cooldownOptions", new CooldownOptions());
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("cooldownOptions", cooldownOptions);
        return map;
    }

    protected SkillResult getSkillResult(UUID id, ItemStack item, LivingEntity... e) {
        boolean cancelled = false;
        int currentCooldown = SupremeItem.getInstance().getCooldownManager().getCooldown(e[0], id, getClass());
        if (currentCooldown == 0) {
            consumeIfConsumable(id, item);
            cancelled = executeExactSkill(e);
            cooldown(e[0], id);
        } else {
            if (e[0] instanceof Player) {
                SupremeItem.getInstance().getCooldownManager().switchCooldownContext((Player) e[0], id,
                        getClass(), cooldownOptions);
            }
        }
        return cancelled ? SkillResult.CANCELLED : SkillResult.SUCCESS;
    }

    protected void cooldown(LivingEntity e, UUID id) {
        if (cooldownOptions.cooldown > 0) {
            SupremeItem.getInstance().getCooldownManager().addCooldown(e,
                    new CooldownManager.CooldownInfo(id, cooldownOptions.cooldown, getClass()), cooldownOptions);
        }
    }

    protected abstract boolean executeExactSkill(LivingEntity... e);

    protected boolean executeCasterActions(LivingEntity e, List<Action> actions) {
        return actions.stream().filter(action -> action.execute(new EntityTarget(e),
                new EntitySource(e)).equals(Action.ActionResult.CANCELLED)).count() > 0;
    }

    protected boolean executeRayCastActions(LivingEntity e, int onRayCastMaxDistance, List<Action> onRayCastPointActions) {
        RayTraceResult result = e.rayTraceBlocks(onRayCastMaxDistance);
        if (result != null) {
            Location l = result.getHitPosition().toLocation(e.getWorld());
            return onRayCastPointActions.stream().filter(action ->
                    action.execute(new LocationTarget(l),
                            new EntitySource(e)).equals(Action.ActionResult.CANCELLED)).count() > 0;
        }
        return false;
    }

    @Getter
    public static class CooldownOptions implements Model {
        protected static final int COOLDOWN_DEFAULT = 0;
        protected static final boolean COOLDOWN_MESSAGE_DEFAULT = true;
        protected static final String COOLDOWN_MESSAGE_FORMAT_DEFAULT = "&2Cooldown &6[%bar&6]";
        protected static final String COOLDOWN_MESSAGE_BAR_DEFAULT = "|";
        protected static final int COOLDOWN_MESSAGE_BAR_COUNT_DEFAULT = 30;

        protected static final String COOLDOWN_MESSAGE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";

        private static final String FORMAT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZlMTk3MmYyY2ZhNGQzMGRjMmYzNGU4ZDIxNTM1OGMwYzU3NDMyYTU1ZjZjMzdhZDkxZTBkZDQ0MTkxYSJ9fX0=";
        private static final String BAR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjcxYjVjYTNhNjFiZWYyOTE2NWViMTI2NmI0MDVhYzI1OTE1NzJjMTZhNGIzOWNiMzZlZGFmNDZjODZjMDg4In19fQ==";
        private static final String BAR_COUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

        @Serializable(headTexture = COOLDOWN_HEAD, description = "gui.skill.cooldown.cooldown")
        @Serializable.Number(minValue = 0, scale = 1)
        @Serializable.Optional(defaultValue = "COOLDOWN_DEFAULT")
        protected int cooldown;

        @Serializable(headTexture = COOLDOWN_MESSAGE_HEAD, description = "gui.skill.cooldown.cooldown-message")
        @Serializable.Optional(defaultValue = "COOLDOWN_MESSAGE_DEFAULT")
        protected boolean cooldownMessage;

        @Serializable(headTexture = FORMAT_HEAD, description = "gui.skill.cooldown.format")
        @Serializable.Optional(defaultValue = "COOLDOWN_MESSAGE_FORMAT_DEFAULT")
        protected String cooldownMessageFormat;

        @Serializable(headTexture = BAR_HEAD, description = "gui.skill.cooldown.bar")
        @Serializable.Optional(defaultValue = "COOLDOWN_MESSAGE_BAR_DEFAULT")
        protected String cooldownMessageBar;

        @Serializable(headTexture = BAR_COUNT_HEAD, description = "gui.skill.cooldown.bar-count")
        @Serializable.Optional(defaultValue = "COOLDOWN_MESSAGE_BAR_COUNT_DEFAULT")
        protected int cooldownMessageBarCount;

        public CooldownOptions() {
            this(COOLDOWN_DEFAULT, COOLDOWN_MESSAGE_DEFAULT, COOLDOWN_MESSAGE_FORMAT_DEFAULT,
                    COOLDOWN_MESSAGE_BAR_DEFAULT, COOLDOWN_MESSAGE_BAR_COUNT_DEFAULT);
        }

        public CooldownOptions(int cooldown, boolean cooldownMessage, String cooldownMessageFormat,
                               String cooldownMessageBar, int cooldownMessageBarCount) {
            this.cooldown = cooldown;
            this.cooldownMessage = cooldownMessage;
            this.cooldownMessageFormat = cooldownMessageFormat;
            this.cooldownMessageBar = cooldownMessageBar;
            this.cooldownMessageBarCount = cooldownMessageBarCount;
        }

        public CooldownOptions(Map<String, Object> map) {
            this.cooldown = (int) map.getOrDefault("cooldown", COOLDOWN_DEFAULT);
            this.cooldownMessage = (boolean) map.getOrDefault("cooldownMessage", COOLDOWN_MESSAGE_DEFAULT);
            this.cooldownMessageFormat = (String) map.getOrDefault("cooldownMessageFormat",
                    COOLDOWN_MESSAGE_FORMAT_DEFAULT);
            this.cooldownMessageBar = (String) map.getOrDefault("cooldownMessageBar", COOLDOWN_MESSAGE_BAR_DEFAULT);
            this.cooldownMessageBarCount = (int) map.getOrDefault("cooldownMessageBarCount",
                    COOLDOWN_MESSAGE_BAR_COUNT_DEFAULT);
        }
    }
}
