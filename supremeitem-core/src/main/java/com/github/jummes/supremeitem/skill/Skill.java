package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.Lists;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

@Enumerable.Parent(classArray = {CooldownSkill.class, LeftClickSkill.class, RightClickSkill.class, HitEntitySkill.class,
        TimerSkill.class, DamageEntitySkill.class, EntitySneakSkill.class})
public abstract class Skill implements Model {

    protected static final List<Action> ACTIONS_DEFAULT = Lists.newArrayList();
    protected static final boolean CONSUMABLE_DEFAULT = false;
    protected static final String CONSUMABLE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTg0YTY4ZmQ3YjYyOGQzMDk2NjdkYjdhNTU4NTViNTRhYmMyM2YzNTk1YmJlNDMyMTYyMTFiZTVmZTU3MDE0In19fQ==";

    @Serializable(headTexture = CONSUMABLE_HEAD, description = "gui.skill.consumable")
    @Serializable.Optional(defaultValue = "CONSUMABLE_DEFAULT")
    protected boolean consumable;

    public Skill(boolean consumable) {
        this.consumable = consumable;
    }

    @Override
    public boolean equals(Object obj) {
        return getClass().equals(obj.getClass());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    protected void consumeIfConsumable(UUID id, ItemStack item) {
        if (consumable) {
            int amount = item.getAmount();
            item.setAmount(--amount);
        }
    }

    public enum SkillResult {
        SUCCESS,
        CANCELLED,
        FAILURE
    }
}
