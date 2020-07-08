package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.entity.DamageAction;
import com.github.jummes.supremeitem.action.source.LocationSource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.entity.selector.EntitySelector;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@AllArgsConstructor
@Enumerable.Child
public class AreaEntitiesAction extends MetaAction {

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = HEAD)
    private Action action;
    @Serializable(headTexture = HEAD)
    private double maxDistance;
    @Serializable(headTexture = HEAD)
    private List<EntitySelector> selectors;
    @Serializable(headTexture = HEAD)
    private boolean castFromLocation;

    public AreaEntitiesAction() {
        this(new DamageAction(), 3.0, Lists.newArrayList(), true);
    }

    public static AreaEntitiesAction deserialize(Map<String, Object> map) {
        Action action = (Action) map.getOrDefault("action", new DamageAction());
        double maxDistance = (double) map.getOrDefault("maxDistance", 3.0);
        List<EntitySelector> selectors = (List<EntitySelector>) map.getOrDefault("selectors", Lists.newArrayList());
        boolean castFromLocation = (boolean) map.getOrDefault("castFromLocation", true);
        return new AreaEntitiesAction(action, maxDistance, selectors, castFromLocation);
    }

    @Override
    protected void execute(Target target, Source source) {
        Location l = null;
        if (target instanceof LocationTarget) {
            l = ((LocationTarget) target).getTarget();
        } else if (target instanceof EntityTarget) {
            l = ((EntityTarget) target).getTarget().getLocation();
        }
        if (l != null) {
            Predicate<LivingEntity> select = selectors.stream().map(selector -> selector.getFilter(source)).
                    reduce(e -> true, Predicate::and);
            Location finalL = l;
            l.getWorld().getNearbyEntities(l, maxDistance, maxDistance, maxDistance).stream().
                    filter(entity -> entity instanceof LivingEntity && select.test((LivingEntity) entity)).
                    forEach(entity -> action.executeAction(new EntityTarget((LivingEntity) entity), castFromLocation ?
                            new LocationSource(finalL) : source));
        }
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(LocationTarget.class, EntityTarget.class);
    }
}
