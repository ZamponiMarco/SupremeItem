package com.github.jummes.supremeitem.entity;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.Location;

@Enumerable.Parent(classArray = {NoEntity.class, GenericEntity.class, ItemEntity.class, FallingBlockEntity.class})
public abstract class Entity implements Model, Cloneable {

    public abstract org.bukkit.entity.Entity spawnEntity(Location l, Target target, Source source);

    public abstract Entity clone();

}
