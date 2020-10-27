package com.github.jummes.supremeitem.placeholder.material;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.block.BlockPlaceholder;
import com.github.jummes.supremeitem.placeholder.block.LocationBlockPlaceholder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Enumerable.Child
@Getter
@Setter
@Enumerable.Displayable(name = "&c&lBlock Type", description = "gui.placeholder.material.block.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ZmOTgxN2Q3NjdkMmVkZTcxODFhMDU3YWEyNmYwOGY3ZWNmNDY1MWRlYzk3ZGU1YjU0ZWVkZTFkZDJiNDJjNyJ9fX0")
public class BlockMaterialPlaceholder extends MaterialPlaceholder {

    private static final String BLOCK_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ZmOTgxN2Q3NjdkMmVkZTcxODFhMDU3YWEyNmYwOGY3ZWNmNDY1MWRlYzk3ZGU1YjU0ZWVkZTFkZDJiNDJjNyJ9fX0";

    @Serializable(headTexture = BLOCK_HEAD, description = "gui.placeholder.material.block.placeholder", additionalDescription = {"gui.additional-tooltips.recreate"})
    private BlockPlaceholder placeholder;

    public BlockMaterialPlaceholder(boolean target, BlockPlaceholder placeholder) {
        super(target);
        this.placeholder = placeholder;
    }

    public BlockMaterialPlaceholder() {
        this(TARGET_DEFAULT, new LocationBlockPlaceholder());
    }

    public static BlockMaterialPlaceholder deserialize(Map<String, Object> map) {
        BlockPlaceholder placeholder = (BlockPlaceholder) map.get("placeholder");
        return new BlockMaterialPlaceholder(TARGET_DEFAULT, placeholder);
    }

    @Override
    public Material computePlaceholder(Target target, Source source) {
        return placeholder.computePlaceholder(target, source).getType();
    }

    @Override
    public String getName() {
        return String.format("%s Type", placeholder.getName());
    }

    @Override
    public MaterialPlaceholder clone() {
        return new BlockMaterialPlaceholder(TARGET_DEFAULT, placeholder.clone());
    }

    @Override
    public ItemStack targetItem() {
        return null;
    }
}
