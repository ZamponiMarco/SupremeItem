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

@AllArgsConstructor
@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lOn Entity Sneak", description = "gui.skill.entity-sneak.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTQyMzI5YTljNDEwNDA4NDE5N2JkNjg4NjE1ODUzOTg0ZDM3ZTE3YzJkZDIzZTNlNDEyZGQ0MmQ3OGI5OGViIn19fQ")
public class EntitySneakSkill extends Skill {

    private static final String CASTER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjY4YjQzMTE1MmU4MmFmNWRlZjg4ZjkxYmI2MWM2MjNiM2I3YWMzYWJlODJkMjc2ZmFkMzQ3Nzc2NDBmOTU5MCJ9fX0=";

    @Serializable(headTexture = CASTER_HEAD, description = "gui.skill.left-click.caster-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> onEntityActions;

    @Serializable(headTexture = COOLDOWN_HEAD, description = "gui.skill.cooldown")
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "COOLDOWN_DEFAULT")
    private int cooldown;

    @Serializable(headTexture = COOLDOWN_MESSAGE_HEAD, description = "gui.skill.cooldown-message")
    @Serializable.Optional(defaultValue = "COOLDOWN_MESSAGE_DEFAULT")
    private boolean cooldownMessage;

    public EntitySneakSkill() {
        this(Lists.newArrayList(), COOLDOWN_DEFAULT, COOLDOWN_MESSAGE_DEFAULT);
    }

    public static EntitySneakSkill deserialize(Map<String, Object> map) {
        List<Action> onEntityActions = (List<Action>) map.getOrDefault("onEntityActions", Lists.newArrayList());
        int cooldown = (int) map.getOrDefault("cooldown", COOLDOWN_DEFAULT);
        boolean cooldownMessage = (boolean) map.getOrDefault("cooldownMessage", COOLDOWN_MESSAGE_DEFAULT);
        return new EntitySneakSkill(onEntityActions, cooldown, cooldownMessage);
    }

    public SkillResult executeSkill(LivingEntity e, UUID id, ItemStack item) {
        boolean cancelled = false;
        int currentCooldown = SupremeItem.getInstance().getCooldownManager().getCooldown(e, id);
        if (currentCooldown == 0) {
            consumeIfConsumable(id, item);
            cancelled = executeCasterActions(e, onEntityActions);
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
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTQyMzI5YTljNDEwNDA4NDE5N2JkNjg4NjE1ODUzOTg0ZDM3ZTE3YzJkZDIzZTNlNDEyZGQ0MmQ3OGI5OGViIn19fQ"),
                "&cEntity Sneak &6&lskill", Libs.getLocale().getList("gui.skill.description"));
    }

}
