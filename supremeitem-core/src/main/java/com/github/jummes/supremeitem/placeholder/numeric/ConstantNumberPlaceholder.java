package com.github.jummes.supremeitem.placeholder.numeric;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@Enumerable.Child
public class ConstantNumberPlaceholder extends NumericPlaceholder {

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.area-entities.actions")
    private double value;

    public ConstantNumberPlaceholder() {
        this(10.0);
    }

    public static ConstantNumberPlaceholder deserialize(Map<String, Object> map) {
        double value = (double) map.get("value");
        return new ConstantNumberPlaceholder(value);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        return value;
    }
}
