package com.github.jummes.supremeitem.item.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.manager.CooldownManager;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lOn entity hit", description = "gui.skill.hit-entity.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=")
public class HitEntitySkill extends Skill {

    private static final String CUSTOM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFkY2Y4NWRlYTg0NDFmN2FmMjg3ZmU3ZTAyMTFjNzRmYzY5YzI5MjNlZDQ5YTE2ZjZkZDFiOWU4MWEyNDlkMyJ9fX0=";

    @Serializable(headTexture = CUSTOM_HEAD, description = "gui.skill.hit-entity.damager-actions")
    protected List<Action> onDamagerActions;
    @Serializable(headTexture = CUSTOM_HEAD, description = "gui.skill.hit-entity.damaged-actions")
    protected List<Action> onDamagedActions;
    @Serializable(headTexture = CUSTOM_HEAD, description = "gui.skill.hit-entity.cooldown")
    protected int cooldown;

    public HitEntitySkill() {
        this(Lists.newArrayList(), Lists.newArrayList(), 0);
    }

    public static HitEntitySkill deserialize(Map<String, Object> map) {
        List<Action> onDamagerActions = (List<Action>) map.get("onDamagerActions");
        List<Action> onDamagedActions = (List<Action>) map.get("onDamagedActions");
        int cooldown = (int) map.get("cooldown");
        return new HitEntitySkill(onDamagerActions, onDamagedActions, cooldown);
    }

    public void executeSkill(LivingEntity damager, LivingEntity damaged, UUID id, ItemStack item) {
        int cooldown = SupremeItem.getInstance().getCooldownManager().getCooldown(damager, id);
        if (cooldown == 0) {
            boolean consumable = SupremeItem.getInstance().getItemManager().getById(id).isConsumable();
            if (consumable) {
                int amount = item.getAmount();
                item.setAmount(--amount);
            }
            onDamagerActions.forEach(Action -> Action.executeAction(new EntityTarget(damager), new EntitySource(damager)));
            onDamagedActions.forEach(Action -> Action.executeAction(new EntityTarget(damaged), new EntitySource(damager)));
            cooldown(damager, id);
        } else {
            damager.sendMessage("Cooldown: " + Math.ceil(cooldown / 20.0));
        }
    }

    private void cooldown(LivingEntity e, UUID id) {
        if (cooldown > 0) {
            SupremeItem.getInstance().getCooldownManager().addCooldown(e,
                    new CooldownManager.CooldownInfo(id, cooldown));
        }
    }

    @Override
    public ItemStack getGUIItem() {
        return new ItemStack(Material.DIAMOND_SWORD);
    }
}
