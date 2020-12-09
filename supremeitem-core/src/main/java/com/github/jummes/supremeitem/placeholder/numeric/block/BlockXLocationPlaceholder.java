package com.github.jummes.supremeitem.placeholder.numeric.block;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.block.BlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.block.LocationBlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.block.Block;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lBlock X Placeholder", description = "gui.placeholder.double.block.x.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxZDZlZGE4M2VkMmMyNGRjZGNjYjFlMzNkZjM2OTRlZWUzOTdhNTcwMTIyNTViZmM1NmEzYzI0NGJjYzQ3NCJ9fX0")
public class BlockXLocationPlaceholder extends NumericBlockPlaceholder {

    public BlockXLocationPlaceholder() {
        this(TARGET_DEFAULT, new LocationBlockPlaceholder());
    }

    public BlockXLocationPlaceholder(boolean target, BlockPlaceholder placeholder) {
        super(target, placeholder);
    }

    public BlockXLocationPlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Double getDoubleValueFromBlock(Block b) {
        return (double) b.getX();
    }

    @Override
    public String getName() {
        return String.format("%s X", placeholder.getName());
    }

    @Override
    public NumericPlaceholder clone() {
        return new BlockXLocationPlaceholder(TARGET_DEFAULT, placeholder.clone());
    }
}
