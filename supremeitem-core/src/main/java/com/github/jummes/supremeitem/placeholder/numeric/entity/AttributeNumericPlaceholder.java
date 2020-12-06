package com.github.jummes.supremeitem.placeholder.numeric.entity;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

public abstract class AttributeNumericPlaceholder extends EntityNumericPlaceholder {
    public AttributeNumericPlaceholder(boolean target) {
        super(target);
    }

    public double getAttributeValue(LivingEntity entity, Attribute attribute) {
        AttributeInstance attr = entity.getAttribute(attribute);
        if (attr == null) {
            return Double.NaN;
        }
        return attr.getValue();
    }
}
