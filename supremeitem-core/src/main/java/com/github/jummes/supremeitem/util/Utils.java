package com.github.jummes.supremeitem.util;

import com.google.common.collect.Lists;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class Utils {
    public static List<ItemStack> getEntityItems(LivingEntity e) {
        EntityEquipment equipment = e.getEquipment();
        if (equipment == null) {
            return Lists.newArrayList();
        }

        List<ItemStack> list = Lists.newArrayList();
        list.add(equipment.getItemInMainHand());
        list.add(equipment.getItemInOffHand());
        list.add(equipment.getBoots());
        list.add(equipment.getLeggings());
        list.add(equipment.getChestplate());
        list.add(equipment.getHelmet());

        return list;
    }
}
