package com.github.jummes.supremeitem.placeholder.bool;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import lombok.AllArgsConstructor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.InventoryHolder;

import java.util.Map;

@AllArgsConstructor
public class PossessItemPlaceholder extends BooleanPlaceholder {

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.placeholder.target")
    private boolean target;
    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.placeholder.target")
    private ItemStackWrapper item;

    public PossessItemPlaceholder() {
        this(false, new ItemStackWrapper());
    }

    public static PossessItemPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.get("target");
        ItemStackWrapper item = (ItemStackWrapper) map.get("item");
        return new PossessItemPlaceholder(target, item);
    }

    @Override
    public Boolean computePlaceholder(Target target, Source source) {
        if (this.target) {
            if (target instanceof EntityTarget) {
                LivingEntity entity = ((EntityTarget) target).getTarget();
                if (entity instanceof InventoryHolder) {
                    return ((InventoryHolder) entity).getInventory().contains(item.getWrapped());
                }
            }
        }
        if (source instanceof EntitySource) {
            LivingEntity entity = ((EntitySource) source).getSource();
            if (entity instanceof InventoryHolder) {
                return ((InventoryHolder) entity).getInventory().contains(item.getWrapped());
            }
        }
        return false;
    }
}
