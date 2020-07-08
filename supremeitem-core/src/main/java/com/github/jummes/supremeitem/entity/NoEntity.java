package com.github.jummes.supremeitem.entity;

import com.github.jummes.libs.annotation.Enumerable;
import org.bukkit.Location;

import java.util.Map;

@Enumerable.Child
public class NoEntity extends Entity {
    public static NoEntity deserialize(Map<String, Object> map) {
        return new NoEntity();
    }

    @Override
    public org.bukkit.entity.Entity spawnEntity(Location l) {
        return null;
    }
}
