package com.github.jummes.supremeitem.placeholder.vector.operator;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.vector.VectorPlaceholder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Enumerable.Displayable
@Enumerable.Parent(classArray = {VectorSumPlaceholder.class})
public abstract class VectorOperatorPlaceholder extends VectorPlaceholder {
    public VectorOperatorPlaceholder(boolean target) {
        super(target);
    }

    public VectorOperatorPlaceholder(Map<String, Object> map) {
        super(map);
    }

    // TODO

    @Override
    public ItemStack targetItem() {
        return null;
    }
}
