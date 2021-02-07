package com.github.jummes.supremeitem.placeholder.bool;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.Placeholder;

import java.util.Map;

@Enumerable.Parent(classArray = {OnGroundPlaceholder.class, PossessItemPlaceholder.class,
        PlayerPropertyBooleanPlaceholder.class, IsWeatherClearPlaceholder.class})
public abstract class BooleanPlaceholder extends Placeholder<Boolean> {

    public BooleanPlaceholder(boolean target) {
        super(target);
    }

    public BooleanPlaceholder(Map<String, Object> map) {
        super(map);
    }

    public abstract BooleanPlaceholder clone();
}
