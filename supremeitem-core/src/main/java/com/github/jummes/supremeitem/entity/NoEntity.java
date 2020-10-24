package com.github.jummes.supremeitem.entity;

import com.github.jummes.libs.annotation.Enumerable;
import org.bukkit.Location;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lNo entity", description = "gui.entity.no-entity.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWZkMjQwMDAwMmFkOWZiYmJkMDA2Njk0MWViNWIxYTM4NGFiOWIwZTQ4YTE3OGVlOTZlNGQxMjlhNTIwODY1NCJ9fX0=")
public class NoEntity extends Entity {
    public static NoEntity deserialize(Map<String, Object> map) {
        return new NoEntity();
    }

    @Override
    public org.bukkit.entity.Entity spawnEntity(Location l) {
        return null;
    }

    @Override
    public Entity clone() {
        return new NoEntity();
    }
}
