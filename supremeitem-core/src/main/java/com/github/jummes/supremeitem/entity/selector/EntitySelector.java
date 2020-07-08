package com.github.jummes.supremeitem.entity.selector;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.action.source.Source;
import org.bukkit.entity.LivingEntity;

import java.util.function.Predicate;

@Enumerable.Parent(classArray = {SourceSelector.class})
public abstract class EntitySelector implements Model {

    public abstract Predicate<LivingEntity> getFilter(Source source);

}
