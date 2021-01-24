package com.github.jummes.supremeitem.placeholder.numeric;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.Placeholder;
import com.github.jummes.supremeitem.placeholder.numeric.block.NumericBlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.entity.EntityNumericPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.item.ItemNumericPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.location.LocationPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.mob.MobNumericPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.operator.NumberOperatorPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.vector.NumericVectorPlaceholder;

import java.util.Map;

@Enumerable.Parent(classArray = {EntityNumericPlaceholder.class, NumberOperatorPlaceholder.class, LocationPlaceholder.class,
        NumericBlockPlaceholder.class, SavedNumericPlaceholder.class, MobNumericPlaceholder.class, ItemNumericPlaceholder.class,
        NumericVectorPlaceholder.class})
public abstract class NumericPlaceholder extends Placeholder<Double> {

    public NumericPlaceholder(boolean target) {
        super(target);
    }

    public NumericPlaceholder(Map<String, Object> map) {
        super(map);
    }

    public abstract NumericPlaceholder clone();

}
