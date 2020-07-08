package com.github.jummes.supremeitem.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@Enumerable.Child
public class ItemEntity extends Entity {

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = HEAD)
    private ItemStackWrapper item;

    public ItemEntity() {
        this(new ItemStackWrapper(new ItemStack(Material.CARROT)));
    }

    public static ItemEntity deserialize(Map<String, Object> map) {
        ItemStackWrapper item = (ItemStackWrapper) map.get("item");
        return new ItemEntity(item);
    }

    @Override
    public org.bukkit.entity.Entity spawnEntity(Location l) {
        Item item = l.getWorld().dropItem(l, this.item.getWrapped());
        item.setVelocity(new Vector());
        item.setGravity(false);
        item.setPickupDelay(37687);
        return item;
    }
}
