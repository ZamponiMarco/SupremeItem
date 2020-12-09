package com.github.jummes.supremeitem.placeholder.numeric.block;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.block.BlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.block.LocationBlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Enumerable.Parent(classArray = {BlockXLocationPlaceholder.class, BlockYLocationPlaceholder.class, BlockZLocationPlaceholder.class})
@Enumerable.Displayable(name = "&c&lNumeric Block Placeholders", description = "gui.placeholder.double.block.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ZmOTgxN2Q3NjdkMmVkZTcxODFhMDU3YWEyNmYwOGY3ZWNmNDY1MWRlYzk3ZGU1YjU0ZWVkZTFkZDJiNDJjNyJ9fX0")
public abstract class NumericBlockPlaceholder extends NumericPlaceholder {

    private static final String BLOCK_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ZmOTgxN2Q3NjdkMmVkZTcxODFhMDU3YWEyNmYwOGY3ZWNmNDY1MWRlYzk3ZGU1YjU0ZWVkZTFkZDJiNDJjNyJ9fX0";

    @Serializable(headTexture = BLOCK_HEAD, description = "gui.placeholder.double.block.placeholder")
    protected BlockPlaceholder placeholder;

    public NumericBlockPlaceholder() {
        this(TARGET_DEFAULT, new LocationBlockPlaceholder());
    }

    public NumericBlockPlaceholder(boolean target, BlockPlaceholder placeholder) {
        super(target);
        this.placeholder = placeholder;
    }

    public NumericBlockPlaceholder(Map<String, Object> map) {
        super(map);
        this.placeholder = (BlockPlaceholder) map.get("placeholder");
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        Block b = placeholder.computePlaceholder(target, source);
        return getDoubleValueFromBlock(b);
    }

    public abstract Double getDoubleValueFromBlock(Block b);

    @Override
    public ItemStack targetItem() {
        return null;
    }
}
