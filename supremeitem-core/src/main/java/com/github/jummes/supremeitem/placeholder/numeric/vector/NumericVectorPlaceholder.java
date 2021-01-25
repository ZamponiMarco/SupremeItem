package com.github.jummes.supremeitem.placeholder.numeric.vector;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import com.github.jummes.supremeitem.placeholder.vector.VectorPlaceholder;
import com.github.jummes.supremeitem.placeholder.vector.entity.DirectionPlaceholder;

import java.util.Map;

@Enumerable.Displayable(name = "&c&lNumeric Vector Placeholders", description = "gui.placeholder.double.vector.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q0ZTZjMzkyZTE0ZjM0YjZhMzFlMzYzNWE4YmYxOGQ5NzNlZWY2ZGM5YjFhMzUxOTQ1MTQ5NzE5N2Q0YyJ9fX0=")
@Enumerable.Parent(classArray = {VectorYPlaceholder.class, VectorXPlaceholder.class, VectorZPlaceholder.class})
public abstract class NumericVectorPlaceholder extends NumericPlaceholder {

    protected VectorPlaceholder placeholder;

    public NumericVectorPlaceholder(boolean target, VectorPlaceholder placeholder) {
        super(target);
        this.placeholder = placeholder;
    }

    public NumericVectorPlaceholder(Map<String, Object> map) {
        super(map);
        this.placeholder = (VectorPlaceholder) map.getOrDefault("placeholder", new DirectionPlaceholder());
    }
}
