package com.github.jummes.supremeitem.action.targeter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public class ItemTarget implements Target {

    private final ItemStack target;
    private final Entity owner;

    @Override
    public Location getLocation() {
        return owner.getLocation();
    }
}
