package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lGive Item", description = "gui.action.entity.give-item.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTJiMzViZGE1ZWJkZjEzNWY0ZTcxY2U0OTcyNmZiZWM1NzM5ZjBhZGVkZjAxYzUxOWUyYWVhN2Y1MTk1MWVhMiJ9fX0=")
@Getter
@Setter
public class GiveItemAction extends EntityAction {

    private static final NumericValue AMOUNT_DEFAULT = new NumericValue(1);

    private static final String AMOUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(displayItem = "getFlatItem", description = "gui.action.entity.give-item.item", additionalDescription = {"gui.additional-tooltips.item"})
    private ItemStackWrapper item;

    @Serializable(headTexture = AMOUNT_HEAD, description = "gui.action.entity.give-item.amount", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Optional(defaultValue = "AMOUNT_DEFAULT")
    @Serializable.Number(minValue = 1, scale = 1)
    private NumericValue amount;

    @SneakyThrows
    public GiveItemAction() {
        this(TARGET_DEFAULT, new ItemStackWrapper(), AMOUNT_DEFAULT.clone());
    }

    public GiveItemAction(boolean target, ItemStackWrapper item, NumericValue amount) {
        super(target);
        this.item = item;
        this.amount = amount;
    }

    public GiveItemAction(Map<String, Object> map) {
        super(map);
        this.item = (ItemStackWrapper) map.get("item");
        this.amount = (NumericValue) map.getOrDefault("amount", AMOUNT_DEFAULT.clone());
    }

    @Override
    public ActionResult execute(Target target, Source source, Map<String, Object> map) {
        LivingEntity e = getEntity(target, source);
        if (e instanceof InventoryHolder) {
            int toAddNumber = amount.getRealValue(target, source).intValue();
            Inventory inventory = ((InventoryHolder) e).getInventory();
            ItemStack toAdd = item.getWrapped().clone();
            toAdd.setAmount(toAddNumber);
            inventory.addItem(toAdd);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAILURE;
    }

    @Override
    public String getName() {
        return "&6&lGive item";
    }

    public ItemStack getFlatItem() {
        return item.getWrapped().clone();
    }

    @Override
    public Action clone() {
        return new GiveItemAction(target, new ItemStackWrapper(item.getWrapped().clone(), true), amount.clone());
    }
}
