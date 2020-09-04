package com.github.jummes.supremeitem.util;

import com.google.common.collect.Lists;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class Utils {
    public static List<ItemStack> getEntityItems(LivingEntity e) {

        EntityEquipment equipment = e.getEquipment();
        if (equipment == null) {
            return Lists.newArrayList();
        }

        List<ItemStack> list = Lists.newArrayList(equipment.getArmorContents());
        list.add(e.getEquipment().getItemInMainHand());
        list.add(e.getEquipment().getItemInOffHand());
        list.removeIf(Objects::isNull);
        return list;
    }
}
