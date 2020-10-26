package com.github.jummes.supremeitem.placeholder.string.block;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.block.BlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.block.LocationBlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.string.StringPlaceholder;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

@Enumerable.Parent(classArray = {BlockTypePlaceholder.class})
@Enumerable.Displayable(name = "&c&lString Block Placeholders", description = "gui.placeholder.string.block.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ZmOTgxN2Q3NjdkMmVkZTcxODFhMDU3YWEyNmYwOGY3ZWNmNDY1MWRlYzk3ZGU1YjU0ZWVkZTFkZDJiNDJjNyJ9fX0")
public abstract class StringBlockPlaceholder extends StringPlaceholder {

    private static final String BLOCK_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ZmOTgxN2Q3NjdkMmVkZTcxODFhMDU3YWEyNmYwOGY3ZWNmNDY1MWRlYzk3ZGU1YjU0ZWVkZTFkZDJiNDJjNyJ9fX0";

    @Serializable(headTexture = BLOCK_HEAD, description = "gui.placeholder.string.block.placeholder")
    protected BlockPlaceholder placeholder;

    public StringBlockPlaceholder() {
        this(TARGET_DEFAULT, new LocationBlockPlaceholder());
    }

    public StringBlockPlaceholder(boolean target, BlockPlaceholder placeholder) {
        super(target);
        this.placeholder = placeholder;
    }

    @Override
    public String computePlaceholder(Target target, Source source) {
        Block b = placeholder.computePlaceholder(target, source);
        return getStringValueFromBlock(b);
    }

    public abstract String getStringValueFromBlock(Block b);

    @Override
    public ItemStack targetItem() {
        return null;
    }
}
