package com.github.jummes.supremeitem.entity.sorter;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.entity.Entity;

import java.util.List;
import java.util.Map;

@Enumerable.Parent(classArray = {ProximitySorter.class, RandomSorter.class})
public abstract class EntitySorter implements Model, Cloneable {

    public EntitySorter() {

    }

    public EntitySorter(Map<String, Object> map) {

    }

    public abstract void sortCollection(List<Entity> list, Target target, Source source);

    public abstract EntitySorter clone();
}
