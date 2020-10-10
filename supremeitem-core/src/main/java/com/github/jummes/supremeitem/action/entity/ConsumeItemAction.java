package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericValue;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lConsume Item", description = "gui.action.consume-item.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjFiYjJjM2JkNjVjZGQ4NGE4MDRlY2E5OGI3YTQ2NzM1ZjAxZTZhMWM5MTk5ZDY2NjE2NjNkYmRiNGY1YjQifX19")
public class ConsumeItemAction extends EntityAction {

    private static final int AMOUNT_DEFAULT = 1;

    private static final String AMOUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(displayItem = "getFlatItem", description = "gui.action.consume-item.item")
    private ItemStackWrapper item;
    @Serializable(headTexture = AMOUNT_HEAD, description = "gui.action.consume-item.amount")
    @Serializable.Number(minValue = 1)
    private NumericValue amount;

    public ConsumeItemAction() {
        this(new ItemStackWrapper(), new NumericValue(AMOUNT_DEFAULT));
    }

    public static ConsumeItemAction deserialize(Map<String, Object> map) {
        ItemStackWrapper item = (ItemStackWrapper) map.get("item");
        NumericValue amount;
        try {
            amount = (NumericValue) map.getOrDefault("amount", new NumericValue(AMOUNT_DEFAULT));
        } catch (ClassCastException e) {
            amount = new NumericValue(((Number) map.getOrDefault("amount", AMOUNT_DEFAULT)).doubleValue());
        }
        return new ConsumeItemAction(item, amount);
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        LivingEntity e = ((EntityTarget) target).getTarget();
        if (e instanceof InventoryHolder) {
            int toRemove = (int) amount.getRealValue(target, source);
            Inventory inventory = ((InventoryHolder) e).getInventory();
            List<ItemStack> toConsume = Arrays.stream(inventory.getStorageContents()).filter(item -> ItemUtils.isSimilar(item,
                    this.item.getWrapped())).collect(Collectors.toList());
            while (toRemove > 0 && !toConsume.isEmpty()) {
                ItemStack consumed = toConsume.remove(0);
                if (consumed != null) {
                    int removed = Math.min(toRemove, consumed.getAmount());
                    consumed.setAmount(consumed.getAmount() - removed);
                    toRemove -= removed;
                }
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAILURE;
    }

    public ItemStack getFlatItem() {
        return item.getWrapped().clone();
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjFiYjJjM2JkNjVjZGQ4NGE4MDRlY2E5OGI3YTQ2NzM1ZjAxZTZhMWM5MTk5ZDY2NjE2NjNkYmRiNGY1YjQifX19"),
                "&6&lConsume item", Libs.getLocale().getList("gui.action.description"));
    }
}
