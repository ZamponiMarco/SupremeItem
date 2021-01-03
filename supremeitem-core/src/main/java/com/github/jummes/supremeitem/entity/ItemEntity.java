package com.github.jummes.supremeitem.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lItem entity", description = "gui.entity.item-entity.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTJiMzViZGE1ZWJkZjEzNWY0ZTcxY2U0OTcyNmZiZWM1NzM5ZjBhZGVkZjAxYzUxOWUyYWVhN2Y1MTk1MWVhMiJ9fX0=")
public class ItemEntity extends Entity {

    @Serializable(displayItem = "getFlatItem", description = "gui.entity.item-entity.item",
            additionalDescription = {"gui.additional-tooltips.item"})
    private ItemStackWrapper item;

    public ItemEntity() {
        this(new ItemStackWrapper());
    }

    public ItemEntity(Map<String, Object> map) {
        super(map);
        this.item = (ItemStackWrapper) map.get("item");
    }

    @Override
    public org.bukkit.entity.Entity spawnEntity(Location l, Target target, Source source) {
        Item item = l.getWorld().dropItem(l, this.item.getWrapped());
        item.setVelocity(new Vector());
        item.setGravity(false);
        item.setPickupDelay(37687);
        return item;
    }

    public ItemStack getFlatItem() {
        return item.getWrapped().clone();
    }

    @Override
    public Entity clone() {
        return new ItemEntity(new ItemStackWrapper(item.getWrapped().clone(), true));
    }

    @Override
    public EntityType getType() {
        return EntityType.DROPPED_ITEM;
    }
}
