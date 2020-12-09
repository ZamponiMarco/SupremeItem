package com.github.jummes.supremeitem.area;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

@Enumerable.Parent(classArray = {SphericArea.class, CylindricArea.class})
public abstract class Area implements Model {

    public Area() {
    }

    public Area(Map<String, Object> map) {

    }

    protected static double lengthSq(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }

    protected static double lengthSq(double x, double z) {
        return (x * x) + (z * z);
    }

    public abstract List<Location> getBlocks(Location center, Target target, Source source);

    public abstract String getName();

}
