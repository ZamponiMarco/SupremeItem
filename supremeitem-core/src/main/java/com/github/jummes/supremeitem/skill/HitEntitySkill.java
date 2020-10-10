package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lOn entity hit", description = "gui.skill.hit-entity.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=")
public class HitEntitySkill extends Skill {

    private static final String DAMAGER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=";
    private static final String DAMAGED_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJlMmRkNTM3Y2I3NDM0YzM5MGQwNDgyZmE0NzI0N2NhM2ViNTZmMTlhOTNjMDRjNmM4NTgxMzUyYjhkOTUzOCJ9fX0=";

    @Serializable(headTexture = DAMAGER_HEAD, description = "gui.skill.hit-entity.damager-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    protected List<Action> onDamagerActions;
    @Serializable(headTexture = DAMAGED_HEAD, description = "gui.skill.hit-entity.damaged-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    protected List<Action> onDamagedActions;
    @Serializable(headTexture = COOLDOWN_HEAD, description = "gui.skill.hit-entity.cooldown")
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "COOLDOWN_DEFAULT")
    protected int cooldown;
    @Serializable(headTexture = COOLDOWN_MESSAGE_HEAD, description = "gui.skill.cooldown-message")
    @Serializable.Optional(defaultValue = "COOLDOWN_MESSAGE_DEFAULT")
    protected boolean cooldownMessage;

    public HitEntitySkill() {
        this(Lists.newArrayList(), Lists.newArrayList(), COOLDOWN_DEFAULT, COOLDOWN_MESSAGE_DEFAULT);
    }

    public static HitEntitySkill deserialize(Map<String, Object> map) {
        List<Action> onDamagerActions = (List<Action>) map.getOrDefault("onDamagerActions", Lists.newArrayList());
        List<Action> onDamagedActions = (List<Action>) map.getOrDefault("onDamagedActions", Lists.newArrayList());
        int cooldown = (int) map.getOrDefault("cooldown", COOLDOWN_DEFAULT);
        boolean cooldownMessage = (boolean) map.getOrDefault("cooldownMessage", COOLDOWN_MESSAGE_DEFAULT);
        return new HitEntitySkill(onDamagerActions, onDamagedActions, cooldown, cooldownMessage);
    }

    public SkillResult executeSkill(LivingEntity damager, LivingEntity damaged, UUID id, ItemStack item) {
        boolean cancelled = false;
        int currentCooldown = SupremeItem.getInstance().getCooldownManager().getCooldown(damager, id);
        if (currentCooldown == 0) {
            consumeIfConsumable(id, item);
            cancelled = onDamagedActions.stream().anyMatch(action ->
                    action.executeAction(new EntityTarget(damaged), new EntitySource(damager)).
                            equals(Action.ActionResult.CANCELLED)) ||
                    onDamagerActions.stream().anyMatch(action ->
                            action.executeAction(new EntityTarget(damager), new EntitySource(damaged)).
                                    equals(Action.ActionResult.CANCELLED));
            cooldown(damager, id, cooldown, cooldownMessage);
        } else {
            if (damager instanceof Player) {
                SupremeItem.getInstance().getCooldownManager().switchCooldownContext((Player) damager, id,
                        this.cooldown, cooldownMessage);
            }
        }
        return cancelled ? DamageEntitySkill.SkillResult.CANCELLED : DamageEntitySkill.SkillResult.SUCCESS;
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0"),
                "&cHit entity &6&lskill", Libs.getLocale().getList("gui.skill.description"));
    }
}
