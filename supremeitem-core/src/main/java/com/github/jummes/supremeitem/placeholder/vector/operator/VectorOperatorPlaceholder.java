package com.github.jummes.supremeitem.placeholder.vector.operator;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.vector.VectorPlaceholder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Enumerable.Parent(classArray = {VectorSumPlaceholder.class, VectorMultiplicationPlaceholder.class,
        VectorDifferencePlaceholder.class, VectorNormalizePlaceholder.class, VectorRotatePlaceholder.class})
@Enumerable.Displayable(name = "&c&lVector Operator Placeholders", description = "gui.placeholder.vector.operator.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzhlZTlmZDhjMGM2ZDI5Njc1YTNhZGZkOTRjNzIzZjZkMzA2YjJhNTk4NGU2NWRiNDY3N2JhNmFjNGY5MDIwIn19fQ==")
public abstract class VectorOperatorPlaceholder extends VectorPlaceholder {

    protected static final String ONE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTBhMTllMjNkMjFmMmRiMDYzY2M1NWU5OWFlODc0ZGM4YjIzYmU3NzliZTM0ZTUyZTdjN2I5YTI1In19fQ==";
    protected static final String TWO_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M1OTZhNDFkYWVhNTFiZTJlOWZlYzdkZTJkODkwNjhlMmZhNjFjOWQ1N2E4YmRkZTQ0YjU1OTM3YjYwMzcifX19";

    public VectorOperatorPlaceholder(boolean target) {
        super(target);
    }

    public VectorOperatorPlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public ItemStack targetItem() {
        return null;
    }
}
