package com.github.jummes.supremeitem.placeholder.numeric.block;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.block.BlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.block.LocationBlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import org.bukkit.block.Block;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lBlock Z Placeholder", description = "gui.placeholder.double.block.z.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzA1ZjE4ZDQxNmY2OGU5YmQxOWQ1NWRmOWZhNzQyZWRmYmYxYTUyNWM4ZTI5ZjY1OWFlODMzYWYyMTdkNTM1In19fQ")
public class BlockZLocationPlaceholder extends NumericBlockPlaceholder {

    public BlockZLocationPlaceholder() {
        this(TARGET_DEFAULT, new LocationBlockPlaceholder());
    }

    public BlockZLocationPlaceholder(boolean target, BlockPlaceholder placeholder) {
        super(target, placeholder);
    }

    public static BlockZLocationPlaceholder deserialize(Map<String, Object> map) {
        BlockPlaceholder placeholder = (BlockPlaceholder) map.get("placeholder");
        return new BlockZLocationPlaceholder(TARGET_DEFAULT, placeholder);
    }

    @Override
    public Double getDoubleValueFromBlock(Block b) {
        return (double) b.getZ();
    }

    @Override
    public String getName() {
        return String.format("%s Z", placeholder.getName());
    }

    @Override
    public NumericPlaceholder clone() {
        return new BlockZLocationPlaceholder(TARGET_DEFAULT, placeholder.clone());
    }
}
