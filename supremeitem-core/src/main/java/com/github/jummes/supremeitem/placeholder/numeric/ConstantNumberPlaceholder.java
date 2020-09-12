package com.github.jummes.supremeitem.placeholder.numeric;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lConstant Number Placeholder", description = "gui.placeholder.constant-number.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ")
public class ConstantNumberPlaceholder extends NumericPlaceholder {

    private static final String VALUE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ===";

    @Serializable(headTexture = VALUE_HEAD, description = "gui.placeholder.constant-number.value")
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
