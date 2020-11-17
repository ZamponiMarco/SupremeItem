package com.github.jummes.supremeitem.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.MaterialValue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lFalling Block", description = "gui.entity.falling-block.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTMzOThhYjNjYjY5NmIzNDQzMGJlOTQ0YjE0YWZiZDIyN2ZkODdlOTkwMjZiY2ZjOGI3Mzg3YTg2MWJkZSJ9fX0=")
public class FallingBlockEntity extends Entity {

    private static final String MATERIAL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTMzOThhYjNjYjY5NmIzNDQzMGJlOTQ0YjE0YWZiZDIyN2ZkODdlOTkwMjZiY2ZjOGI3Mzg3YTg2MWJkZSJ9fX0=";

    @Serializable(headTexture = MATERIAL_HEAD, description = "gui.entity.falling-block.material",
            additionalDescription = {"gui.additional-tooltips.value"})
    private MaterialValue material;


    public FallingBlockEntity() {
        this(new MaterialValue(Material.RED_SAND));
    }

    public FallingBlockEntity(MaterialValue material) {
        this.material = material;
    }

    public FallingBlockEntity(Map<String, Object> map) {
        super(map);
        this.material = (MaterialValue) map.getOrDefault("material", new MaterialValue(Material.RED_SAND));
    }

    @Override
    public org.bukkit.entity.Entity spawnEntity(Location l, Target target, Source source) {
        FallingBlock entity = l.getWorld().spawnFallingBlock(l, Bukkit.createBlockData(material.
                getRealValue(target, source)));
        entity.setDropItem(false);
        return entity;
    }

    @Override
    public Entity clone() {
        return new FallingBlockEntity(material);
    }

    @Override
    public EntityType getType() {
        return EntityType.FALLING_BLOCK;
    }
}
