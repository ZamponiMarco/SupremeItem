package com.github.jummes.supremeitem.placeholder.string.block;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.block.BlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.block.LocationBlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.string.StringPlaceholder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;

import java.util.Map;

@Enumerable.Child
@Getter
@Setter
@Enumerable.Displayable(name = "&c&lBlock Type", description = "gui.placeholder.string.block.type.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ZmOTgxN2Q3NjdkMmVkZTcxODFhMDU3YWEyNmYwOGY3ZWNmNDY1MWRlYzk3ZGU1YjU0ZWVkZTFkZDJiNDJjNyJ9fX0")
public class BlockTypePlaceholder extends StringBlockPlaceholder {

    public BlockTypePlaceholder() {
        this(TARGET_DEFAULT, new LocationBlockPlaceholder());
    }

    public BlockTypePlaceholder(boolean target, BlockPlaceholder placeholder) {
        super(target, placeholder);
    }

    public static BlockTypePlaceholder deserialize(Map<String, Object> map) {
        BlockPlaceholder placeholder = (BlockPlaceholder) map.get("placeholder");
        return new BlockTypePlaceholder(TARGET_DEFAULT, placeholder);
    }

    @Override
    public String getStringValueFromBlock(Block b) {
        return b.getType().name();
    }

    @Override
    public String getName() {
        return String.format("%s Type", placeholder.getName());
    }

    @Override
    public StringPlaceholder clone() {
        return new BlockTypePlaceholder(TARGET_DEFAULT, placeholder.clone());
    }
}
