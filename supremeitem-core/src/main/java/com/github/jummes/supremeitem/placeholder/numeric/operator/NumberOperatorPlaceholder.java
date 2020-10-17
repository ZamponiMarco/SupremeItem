package com.github.jummes.supremeitem.placeholder.numeric.operator;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;

@Enumerable.Parent(classArray = {RandomNumberPlaceholder.class})
@Enumerable.Displayable(name = "&c&lNumeric Operator Placeholder", description = "gui.placeholder.double.operator.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzhlZTlmZDhjMGM2ZDI5Njc1YTNhZGZkOTRjNzIzZjZkMzA2YjJhNTk4NGU2NWRiNDY3N2JhNmFjNGY5MDIwIn19fQ==")
public abstract class NumberOperatorPlaceholder extends NumericPlaceholder {
    public NumberOperatorPlaceholder(boolean target) {
        super(target);
    }
}
