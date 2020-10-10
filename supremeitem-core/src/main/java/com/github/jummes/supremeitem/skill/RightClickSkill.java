package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lRight click skill", description = "gui.skill.right-click.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0=")
public class RightClickSkill extends Skill {

    private static final int RAY_CAST_DISTANCE_DEFAULT = 30;

    private static final String CASTER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjY4YjQzMTE1MmU4MmFmNWRlZjg4ZjkxYmI2MWM2MjNiM2I3YWMzYWJlODJkMjc2ZmFkMzQ3Nzc2NDBmOTU5MCJ9fX0=";
    private static final String RAY_CAST_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc4N2I3YWZiNWE1OTk1Mzk3NWJiYTI0NzM3NDliNjAxZDU0ZDZmOTNjZWFjN2EwMmFjNjlhYWU3ZjliOCJ9fX0=";
    private static final String DISTANCE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFhNjg2MGQxMGQ3Yzg2ZmNjYzY1MjhkOWYyNTY0YTJmNTZkNWNmMzdlNzllZDVjNzc4NDk0MDI1MTVkNzc1MSJ9fX0=";

    @Serializable(headTexture = CASTER_HEAD, description = "gui.skill.right-click.caster-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    protected List<Action> onCasterActions;

    @Serializable(headTexture = COOLDOWN_HEAD, description = "gui.skill.cooldown")
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "COOLDOWN_DEFAULT")
    protected int cooldown;

    @Serializable(headTexture = RAY_CAST_HEAD, description = "gui.skill.right-click.ray-cast-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    protected List<Action> onRayCastPointActions;

    @Serializable(headTexture = DISTANCE_HEAD, description = "gui.skill.right-click.ray-cast-distance")
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "RAY_CAST_DISTANCE_DEFAULT")
    protected int onRayCastMaxDistance;

    @Serializable(headTexture = COOLDOWN_MESSAGE_HEAD, description = "gui.skill.cooldown-message")
    @Serializable.Optional(defaultValue = "COOLDOWN_MESSAGE_DEFAULT")
    protected boolean cooldownMessage;

    public RightClickSkill() {
        this(Lists.newArrayList(), COOLDOWN_DEFAULT, Lists.newArrayList(), RAY_CAST_DISTANCE_DEFAULT, COOLDOWN_MESSAGE_DEFAULT);
    }

    public static RightClickSkill deserialize(Map<String, Object> map) {
        List<Action> onCasterActions = (List<Action>) map.getOrDefault("onCasterActions", Lists.newArrayList());
        int cooldown = (int) map.getOrDefault("cooldown", COOLDOWN_DEFAULT);
        List<Action> onRayCastPointActions = (List<Action>) map.getOrDefault("onRayCastPointActions", Lists.newArrayList());
        int onRayCastMaxDistance = (int) map.getOrDefault("onRayCastMaxDistance", RAY_CAST_DISTANCE_DEFAULT);
        boolean cooldownMessage = (boolean) map.getOrDefault("cooldownMessage", COOLDOWN_MESSAGE_DEFAULT);
        return new RightClickSkill(onCasterActions, cooldown, onRayCastPointActions, onRayCastMaxDistance, cooldownMessage);
    }

    public SkillResult executeSkill(LivingEntity e, UUID id, ItemStack item) {
        boolean cancelled = false;
        int currentCooldown = SupremeItem.getInstance().getCooldownManager().getCooldown(e, id);
        if (currentCooldown == 0) {
            consumeIfConsumable(id, item);
            cancelled = executeCasterActions(e, onCasterActions) || executeRayCastActions(e, onRayCastMaxDistance, onRayCastPointActions);
            cooldown(e, id, cooldown, cooldownMessage);
        } else {
            if (e instanceof Player) {
                SupremeItem.getInstance().getCooldownManager().switchCooldownContext((Player) e, id,
                        this.cooldown, cooldownMessage);
            }
        }
        return cancelled ? SkillResult.CANCELLED : SkillResult.SUCCESS;
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0"),
                "&cRight click &6&lskill", Libs.getLocale().getList("gui.skill.description"));
    }
}
