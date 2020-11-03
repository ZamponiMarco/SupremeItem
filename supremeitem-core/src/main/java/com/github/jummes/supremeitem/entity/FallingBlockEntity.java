package com.github.jummes.supremeitem.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.MaterialValue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lFalling Block", description = "gui.entity.falling-block.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTMzOThhYjNjYjY5NmIzNDQzMGJlOTQ0YjE0YWZiZDIyN2ZkODdlOTkwMjZiY2ZjOGI3Mzg3YTg2MWJkZSJ9fX0=")
public class FallingBlockEntity extends Entity {

    private static final String MATERIAL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTMzOThhYjNjYjY5NmIzNDQzMGJlOTQ0YjE0YWZiZDIyN2ZkODdlOTkwMjZiY2ZjOGI3Mzg3YTg2MWJkZSJ9fX0=";

    @Serializable(headTexture = MATERIAL_HEAD, description = "gui.entity.falling-block.material")
    private MaterialValue material;

    public FallingBlockEntity(MaterialValue material) {
        this.material = material;
    }

    public FallingBlockEntity() {
        this(new MaterialValue(Material.RED_SAND));
    }

    public static FallingBlockEntity deserialize(Map<String, Object> map) {
        MaterialValue material;
        try {
            material = (MaterialValue) map.getOrDefault("material", new MaterialValue(Material.RED_SAND));
        } catch (ClassCastException e) {
            material = new MaterialValue(Material.valueOf((String) map.getOrDefault("material", "RED_SAND")));
        }
        return new FallingBlockEntity(material);
    }

    @Override
    public org.bukkit.entity.Entity spawnEntity(Location l, Target target, Source source) {
        return l.getWorld().spawnFallingBlock(l, Bukkit.createBlockData(material.getRealValue(target, source)));
    }

    @Override
    public Entity clone() {
        return new FallingBlockEntity(material);
    }
}
