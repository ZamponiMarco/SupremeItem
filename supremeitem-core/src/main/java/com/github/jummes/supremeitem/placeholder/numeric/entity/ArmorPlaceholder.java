package com.github.jummes.supremeitem.placeholder.numeric.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lArmor Placeholder", description = "gui.placeholder.double.entity.armor.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmJkMzFhZDFlOTNmMTY2Zjk3ODU5Y2Q5ZjJiODBkYTUyYTgyOTQ0MDA5N2Y1ZDY3YThjMjEwZDEyMmI5ZDVlNSJ9fX0=")
public class ArmorPlaceholder extends AttributeNumericPlaceholder {

    public ArmorPlaceholder() {
        this(TARGET_DEFAULT);
    }

    public ArmorPlaceholder(boolean target) {
        super(target);
    }

    public static ArmorPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        return new ArmorPlaceholder();
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        LivingEntity entity = getEntity(target, source);
        if (entity == null) {
            return Double.NaN;
        }

        return getAttributeValue(entity, Attribute.GENERIC_ARMOR);
    }

    @Override
    public String getName() {
        return String.format("%s Armor", target ? "Target" : "Source");
    }

    @Override
    public NumericPlaceholder clone() {
        return new ArmorPlaceholder(target);
    }
}
