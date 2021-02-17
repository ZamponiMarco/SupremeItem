package com.github.jummes.supremeitem.util;

import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.skill.Skill;
import com.google.common.collect.Lists;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.Objects;

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

        Skill.additionalSlots.forEach(slot -> {
            if (e instanceof Player) {
                Player p = (Player) e;
                list.add(p.getInventory().getItem(slot));
            } else {
                list.add(null);
            }
        });

        return list;
    }

    public static Skill getSkill(ModelPath<Item> path) {
        Skill toReturn = null;
        ModelPath<Item> clone = path.clone();
        while (toReturn == null && !(path.getLast() instanceof Skill)) {
            if (clone.getLast() instanceof Skill) {
                toReturn = (Skill) clone.getLast();
            } else {
                clone.popModel();
            }
        }
        return toReturn;
    }

    public static MetadataValue getMetadata(List<MetadataValue> value, Object defaultValue) {
        return value.stream().filter(metadataValue -> Objects.equals(metadataValue.getOwningPlugin(),
                SupremeItem.getInstance())).findFirst().orElse(new FixedMetadataValue(SupremeItem.getInstance(), defaultValue));
    }
}
