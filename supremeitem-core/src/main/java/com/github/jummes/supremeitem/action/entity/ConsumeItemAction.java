package com.github.jummes.supremeitem.action.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lConsume Item", description = "gui.action.consume-item.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjFiYjJjM2JkNjVjZGQ4NGE4MDRlY2E5OGI3YTQ2NzM1ZjAxZTZhMWM5MTk5ZDY2NjE2NjNkYmRiNGY1YjQifX19")
public class ConsumeItemAction extends EntityAction {

    @Serializable(displayItem = "getFlatItem", description = "gui.action.consume-item.item")
    private ItemStackWrapper item;

    public ConsumeItemAction() {
        this(new ItemStackWrapper());
    }

    public static ConsumeItemAction deserialize(Map<String, Object> map) {
        ItemStackWrapper item = (ItemStackWrapper) map.get("item");
        return new ConsumeItemAction(item);
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        LivingEntity e = ((EntityTarget) target).getTarget();
        if (e instanceof InventoryHolder) {
            Inventory inventory = ((InventoryHolder) e).getInventory();
            ItemStack toConsume = Arrays.stream(inventory.getStorageContents()).filter(item -> ItemUtils.isSimilar(item,
                    this.item.getWrapped())).findFirst().orElse(null);
            if (toConsume != null) {
                toConsume.setAmount(toConsume.getAmount() - 1);
                return ActionResult.SUCCESS;
            }
            return ActionResult.FAILURE;
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
