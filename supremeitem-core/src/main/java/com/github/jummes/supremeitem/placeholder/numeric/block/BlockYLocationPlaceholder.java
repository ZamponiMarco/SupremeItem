package com.github.jummes.supremeitem.placeholder.numeric.block;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.block.BlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.block.LocationBlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.block.Block;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lBlock Y Placeholder", description = "gui.placeholder.double.block.y.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODlmZjhjNzQ0OTUwNzI5ZjU4Y2I0ZTY2ZGM2OGVhZjYyZDAxMDZmOGE1MzE1MjkxMzNiZWQxZDU1ZTMifX19")
public class BlockYLocationPlaceholder extends NumericBlockPlaceholder {

    public BlockYLocationPlaceholder() {
        this(TARGET_DEFAULT, new LocationBlockPlaceholder());
    }

    public BlockYLocationPlaceholder(boolean target, BlockPlaceholder placeholder) {
        super(target, placeholder);
    }

    public BlockYLocationPlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Double getDoubleValueFromBlock(Block b) {
        return (double) b.getY();
    }

    @Override
    public String getName() {
        return String.format("%s y", placeholder.getName());
    }

    @Override
    public NumericPlaceholder clone() {
        return new BlockYLocationPlaceholder(TARGET_DEFAULT, placeholder.clone());
    }
}
