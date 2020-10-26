package com.github.jummes.supremeitem.placeholder.block;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.Placeholder;
import org.bukkit.block.Block;

@Enumerable.Parent(classArray = {LocationBlockPlaceholder.class})
public abstract class BlockPlaceholder extends Placeholder<Block> {
    public BlockPlaceholder(boolean target) {
        super(target);
    }

    public abstract BlockPlaceholder clone();
}
