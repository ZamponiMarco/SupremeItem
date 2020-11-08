package com.github.jummes.supremeitem.area;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.Location;

import java.util.List;

@Enumerable.Parent(classArray = {SphericArea.class})
public abstract class Area implements Model {

    public abstract List<Location> getBlocks(Location center, Target target, Source source);

    public abstract String getName();

}
