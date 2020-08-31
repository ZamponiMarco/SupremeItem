package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.manager.CooldownManager;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lRight click skill", description = "gui.skill.right-click.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0=")
public class RightClickSkill extends Skill {

    private static final String CUSTOM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFkY2Y4NWRlYTg0NDFmN2FmMjg3ZmU3ZTAyMTFjNzRmYzY5YzI5MjNlZDQ5YTE2ZjZkZDFiOWU4MWEyNDlkMyJ9fX0=";

    @Serializable(headTexture = CUSTOM_HEAD, description = "gui.skill.right-click.caster-actions")
    protected List<Action> onCasterActions;
    @Serializable(headTexture = CUSTOM_HEAD, description = "gui.skill.right-click.cooldown")
    protected int cooldown;
    @Serializable(headTexture = CUSTOM_HEAD, description = "gui.skill.right-click.ray-cast-actions")
    protected List<Action> onRayCastPointActions;
    @Serializable(headTexture = CUSTOM_HEAD, description = "gui.skill.right-click.ray-cast-distance")
    protected int onRayCastMaxDistance;

    public RightClickSkill() {
        this(Lists.newArrayList(), 0, Lists.newArrayList(), 30);
    }

    public static RightClickSkill deserialize(Map<String, Object> map) {
        List<Action> onCasterActions = (List<Action>) map.get("onCasterActions");
        int cooldown = (int) map.get("cooldown");
        List<Action> onRayCastPointActions = (List<Action>) map.get("onRayCastPointActions");
        int onRayCastMaxDistance = (int) map.get("onRayCastMaxDistance");
        return new RightClickSkill(onCasterActions, cooldown, onRayCastPointActions, onRayCastMaxDistance);
    }

    public void executeSkill(LivingEntity e, UUID id, ItemStack item) {
        int cooldown = SupremeItem.getInstance().getCooldownManager().getCooldown(e, id);
        if (cooldown == 0) {
            boolean consumable = SupremeItem.getInstance().getItemManager().getById(id).isConsumable();
            if (consumable) {
                int amount = item.getAmount();
                item.setAmount(--amount);
            }
            onCasterActions.forEach(Action -> Action.executeAction(new EntityTarget(e), new EntitySource(e)));
            RayTraceResult result = e.rayTraceBlocks(onRayCastMaxDistance);
            if (result != null) {
                Location l = result.getHitPosition().toLocation(e.getWorld());
                onRayCastPointActions.forEach(Action -> Action.executeAction(new LocationTarget(l), new EntitySource(e)));
            }
            cooldown(e, id);
        } else {
            e.sendMessage("Cooldown: " + Math.ceil(cooldown / 20.0));
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
        return ItemUtils.getNamedItem(new ItemStack(Material.STICK), "&6OnInteract", Lists.newArrayList());
    }
}
