package com.github.jummes.supremeitem.action.item;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lDurability", description = "gui.action.item.durability.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI4MzI5ZDdkMWZkZTYyMGViYTY2MTcwZjc4NGFjZmNjNTJjNjQyZmZmZjg0YzY4YWFjMzc1MmU3OGYxNTBiOSJ9fX0=")
public class DurabilityAction extends ItemAction {

    private static final NumericValue DURABILITY_DEFAULT = new NumericValue(10);

    private static final String DURABILITY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI4MzI5ZDdkMWZkZTYyMGViYTY2MTcwZjc4NGFjZmNjNTJjNjQyZmZmZjg0YzY4YWFjMzc1MmU3OGYxNTBiOSJ9fX0=";

    @Serializable(headTexture = DURABILITY_HEAD, description = "gui.action.item.durability.durability", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0, scale = 1)
    private NumericValue durability;

    public DurabilityAction() {
        this(TARGET_DEFAULT, DURABILITY_DEFAULT.clone());
    }

    public DurabilityAction(boolean target, NumericValue durability) {
        super(target);
        this.durability = durability;
    }

    public DurabilityAction(Map<String, Object> map) {
        super(map);
        this.durability = (NumericValue) map.getOrDefault("durability", DURABILITY_DEFAULT.clone());
    }

    @Override
    public ActionResult execute(Target target, Source source, Map<String, Object> map) {
        ItemStack item = getItemStack(target, source);
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof Damageable) {
                Damageable damageable = (Damageable) meta;
                damageable.setDamage(durability.getRealValue(target, source).intValue());
                item.setItemMeta(meta);
                if (damageable.getDamage() > item.getType().getMaxDurability()) {
                    item.setAmount(0);
                }
            }
        }
        return ActionResult.FAILURE;
    }

    @Override
    public String getName() {
        return "&6&lSet Item Durability: &c" + durability.getName();
    }

    @Override
    public Action clone() {
        return new DurabilityAction(target, durability.clone());
    }
}
