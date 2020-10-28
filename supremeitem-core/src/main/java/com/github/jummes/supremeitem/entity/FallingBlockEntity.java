package com.github.jummes.supremeitem.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lFalling Block", description = "gui.entity.falling-block.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTMzOThhYjNjYjY5NmIzNDQzMGJlOTQ0YjE0YWZiZDIyN2ZkODdlOTkwMjZiY2ZjOGI3Mzg3YTg2MWJkZSJ9fX0=")
public class FallingBlockEntity extends Entity {

    private static final String MATERIAL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTMzOThhYjNjYjY5NmIzNDQzMGJlOTQ0YjE0YWZiZDIyN2ZkODdlOTkwMjZiY2ZjOGI3Mzg3YTg2MWJkZSJ9fX0=";

    @Serializable(headTexture = MATERIAL_HEAD, stringValue = true, description = "gui.entity.falling-block.material", fromListMapper = "materialListMapper", fromList = "materialList")
    private Material material;

    public FallingBlockEntity(Material material) {
        this.material = material;
    }

    public FallingBlockEntity() {
        this(Material.RED_SAND);
    }

    public static FallingBlockEntity deserialize(Map<String, Object> map) {
        Material material = Material.valueOf((String) map.getOrDefault("material", "RED_SAND"));
        return new FallingBlockEntity(material);
    }

    public static List<Object> materialList(ModelPath<?> path) {
        return ItemUtils.getMaterialList().stream().filter(m -> ((Material) m).isBlock()).collect(Collectors.toList());
    }

    public static Function<Object, ItemStack> materialListMapper() {
        return ItemUtils.getMaterialMapper();
    }

    @Override
    public org.bukkit.entity.Entity spawnEntity(Location l) {
        return l.getWorld().spawnFallingBlock(l, Bukkit.createBlockData(material));
    }

    @Override
    public Entity clone() {
        return new FallingBlockEntity(material);
    }
}
