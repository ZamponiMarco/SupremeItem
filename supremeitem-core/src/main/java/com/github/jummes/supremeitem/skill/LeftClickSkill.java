package com.github.jummes.supremeitem.skill;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
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
@Enumerable.Displayable(name = "&c&lLeft click skill", description = "gui.skill.left-click.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWYxMzNlOTE5MTlkYjBhY2VmZGMyNzJkNjdmZDg3YjRiZTg4ZGM0NGE5NTg5NTg4MjQ0NzRlMjFlMDZkNTNlNiJ9fX0=")
public class LeftClickSkill extends Skill {

    private static final String CASTER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjY4YjQzMTE1MmU4MmFmNWRlZjg4ZjkxYmI2MWM2MjNiM2I3YWMzYWJlODJkMjc2ZmFkMzQ3Nzc2NDBmOTU5MCJ9fX0=";
    private static final String COOLDOWN_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";
    private static final String RAY_CAST_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc4N2I3YWZiNWE1OTk1Mzk3NWJiYTI0NzM3NDliNjAxZDU0ZDZmOTNjZWFjN2EwMmFjNjlhYWU3ZjliOCJ9fX0=";
    private static final String DISTANCE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFhNjg2MGQxMGQ3Yzg2ZmNjYzY1MjhkOWYyNTY0YTJmNTZkNWNmMzdlNzllZDVjNzc4NDk0MDI1MTVkNzc1MSJ9fX0=";

    @Serializable(headTexture = CASTER_HEAD, description = "gui.skill.left-click.caster-actions")
    protected List<Action> onCasterActions;
    @Serializable(headTexture = COOLDOWN_HEAD, description = "gui.skill.left-click.cooldown")
    @Serializable.Number(minValue = 0)
    protected int cooldown;
    @Serializable(headTexture = RAY_CAST_HEAD, description = "gui.skill.left-click.ray-cast-actions")
    protected List<Action> onRayCastPointActions;
    @Serializable(headTexture = DISTANCE_HEAD, description = "gui.skill.left-click.ray-cast-distance")
    @Serializable.Number(minValue = 0)
    protected int onRayCastMaxDistance;

    public LeftClickSkill() {
        this(Lists.newArrayList(), 0, Lists.newArrayList(), 30);
    }

    public static LeftClickSkill deserialize(Map<String, Object> map) {
        List<Action> onCasterActions = (List<Action>) map.get("onCasterActions");
        int cooldown = (int) map.get("cooldown");
        List<Action> onRayCastPointActions = (List<Action>) map.get("onRayCastPointActions");
        int onRayCastMaxDistance = (int) map.get("onRayCastMaxDistance");
        return new LeftClickSkill(onCasterActions, cooldown, onRayCastPointActions, onRayCastMaxDistance);
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
            e.sendMessage(Libs.getLocale().get("messages.cooldown").replace("$cooldown", String.valueOf(cooldown / 20.0)));
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
        return ItemUtils.getNamedItem(new ItemStack(Material.STICK),
                "&cLeft click &6&lskill", Lists.newArrayList());
    }
}
