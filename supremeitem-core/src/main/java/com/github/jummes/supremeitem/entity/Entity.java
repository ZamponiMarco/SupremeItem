package com.github.jummes.supremeitem.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.Model;
import org.bukkit.Location;

@Enumerable.Parent(classArray = {NoEntity.class, GenericEntity.class, ItemEntity.class})
public abstract class Entity implements Model {

    public abstract org.bukkit.entity.Entity spawnEntity(Location l);

}
