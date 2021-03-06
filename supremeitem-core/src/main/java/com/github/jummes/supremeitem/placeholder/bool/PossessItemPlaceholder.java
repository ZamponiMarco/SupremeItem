package com.github.jummes.supremeitem.placeholder.bool;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lPossess Item Placeholder", description = "gui.placeholder.boolean.possess-item.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzVlMmUwOTU5NzEyZGNkMzM1N2NjM2NlYTg1Zjk5YjNmZDgwOTc4NTVjNzU0YjliMTcxZjk2MzUxNDIyNWQifX19")
public class PossessItemPlaceholder extends BooleanPlaceholder {

    private static final int AMOUNT_DEFAULT = 1;

    private static final String AMOUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(displayItem = "getFlatItem", description = "gui.placeholder.boolean.possess-item.item",
            additionalDescription = {"gui.additional-tooltips.item"})
    private ItemStackWrapper item;
    @Serializable(headTexture = AMOUNT_HEAD, description = "gui.placeholder.boolean.possess-item.amount")
    @Serializable.Number(minValue = 1)
    @Serializable.Optional(defaultValue = "AMOUNT_DEFAULT")
    private int amount;

    public PossessItemPlaceholder() {
        this(TARGET_DEFAULT, new ItemStackWrapper(), AMOUNT_DEFAULT);
    }

    public PossessItemPlaceholder(boolean target, ItemStackWrapper item, int amount) {
        super(target);
        this.item = item;
        this.amount = amount;
    }

    public PossessItemPlaceholder(Map<String, Object> map) {
        super(map);
        this.item = (ItemStackWrapper) map.getOrDefault("item", new ItemStackWrapper());
        this.amount = (int) map.getOrDefault("amount", AMOUNT_DEFAULT);
    }

    @Override
    public Boolean computePlaceholder(Target target, Source source) {
        if (this.target) {
            if (target instanceof EntityTarget) {
                LivingEntity entity = ((EntityTarget) target).getTarget();
                if (entity instanceof InventoryHolder) {
                    return ((InventoryHolder) entity).getInventory().containsAtLeast(item.getWrapped(), amount);
                }
            }
        }
        LivingEntity entity = source.getCaster();
        if (entity instanceof InventoryHolder) {
            return ((InventoryHolder) entity).getInventory().containsAtLeast(item.getWrapped(), amount);
        }
        return false;
    }

    @Override
    public String getName() {
        return String.format("%s Possesses %s", target ? "Target" : "Source", item.getWrapped().toString());
    }

    public ItemStack getFlatItem() {
        return item.getWrapped().clone();
    }

    @Override
    public BooleanPlaceholder clone() {
        return new PossessItemPlaceholder(target, new ItemStackWrapper(item.getWrapped(), true), amount);
    }
}
