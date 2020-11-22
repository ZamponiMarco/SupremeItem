package com.github.jummes.supremeitem.entity.sorter;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Enumerable.Displayable(name = "&6&lProximity Sorter", description = "gui.entity.sorter.proximity.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE1Mzc2MjIxNGFkOTljMmM1OWYzMGI0YTJhYTI5YTU4NTE2NjNhZDdkZmE4NTZlOGRhNzI1MjFhYWJhNjc4In19fQ==")
public class ProximitySorter extends EntitySorter {

    protected static final boolean TARGET_DEFAULT = true;
    protected static final boolean INVERSE_DEFAULT = false;

    private static final String TARGET_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc4N2I3YWZiNWE1OTk1Mzk3NWJiYTI0NzM3NDliNjAxZDU0ZDZmOTNjZWFjN2EwMmFjNjlhYWU3ZjliOCJ9fX0==";
    private static final String INVERSE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTAxMWU3NTE2ZGFhYzVmMjMyMGY2N2I5N2FkNTMwNGY5MDY2Zjg2NDA3YjU4YTUzMGY4MGY4ZmM5N2IzZTg2ZSJ9fX0=";

    @Serializable(headTexture = TARGET_HEAD, description = "gui.entity.sorter.proximity.target")
    @Serializable.Optional(defaultValue = "TARGET_DEFAULT")
    private boolean target;
    @Serializable(headTexture = TARGET_HEAD, description = "gui.entity.sorter.proximity.inverse")
    @Serializable.Optional(defaultValue = "INVERSE_DEFAULT")
    private boolean inverse;

    public ProximitySorter() {
        this(TARGET_DEFAULT, INVERSE_DEFAULT);
    }

    public ProximitySorter(Map<String, Object> map) {
        super(map);
        this.target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        this.inverse = (boolean) map.getOrDefault("inverse", INVERSE_DEFAULT);
    }

    public ProximitySorter(boolean target, boolean inverse) {
        this.target = target;
        this.inverse = inverse;
    }

    @Override
    public void sortCollection(List<Entity> list, Target target, Source source) {
        list.sort(getComparator(target, source));
    }

    public Comparator<Entity> getComparator(Target target, Source source) {
        return (e1, e2) -> {
            Location l = getLocation(target, source);
            int d = Double.compare(e1.getLocation().distance(l), e2.getLocation().distance(l));
            return this.inverse ? d * -1 : d;
        };
    }

    private Location getLocation(Target target, Source source) {
        if (this.target) {
            return target.getLocation();
        }
        return source.getLocation();
    }

    @Override
    public EntitySorter clone() {
        return new ProximitySorter(target, inverse);
    }
}
