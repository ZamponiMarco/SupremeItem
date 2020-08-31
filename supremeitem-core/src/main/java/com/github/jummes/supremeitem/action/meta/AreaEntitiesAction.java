package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.LocationSource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.entity.selector.EntitySelector;
import com.github.jummes.supremeitem.entity.selector.SourceSelector;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@AllArgsConstructor
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lApply actions to entities in Area", description = "gui.action.area-entities.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZjZDQxNGIwNWE1MzJjNjA5YzJhYTQ4ZDZjMDYyYzI5MmQ1MzNkZmFmNGQ3MzJhYmU5YWY1NzQxNTg5ZSJ9fX0=")
public class AreaEntitiesAction extends MetaAction {

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    private static final String MAX_DISTANCE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    private static final String SELECTOR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM4ZDI3NTk1NjlkNTE1ZDI0NTRkNGE3ODkxYTk0Y2M2M2RkZmU3MmQwM2JmZGY3NmYxZDQyNzdkNTkwIn19fQ==";
    private static final String CAST_LOCATION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQzYjJlM2U5OTU0ZjgyMmI0M2ZlNWY5MTUwOTllMGE2Y2FhYTgxZjc5MTIyMmI1ODAzZDQxNDVhODUxNzAifX19";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.area-entities.actions")
    private List<Action> actions;
    @Serializable(headTexture = MAX_DISTANCE_HEAD, description = "gui.action.area-entities.max-distance")
    private double maxDistance;
    @Serializable(headTexture = SELECTOR_HEAD, description = "gui.action.area-entities.selectors")
    private List<EntitySelector> selectors;
    @Serializable(headTexture = CAST_LOCATION_HEAD, description = "gui.action.area-entities.cast-from-location")
    private boolean castFromLocation;

    public AreaEntitiesAction() {
        this(Lists.newArrayList(), 3.0, Lists.newArrayList(new SourceSelector()), true);
    }

    public static AreaEntitiesAction deserialize(Map<String, Object> map) {
        List<Action> actions = (List<Action>) map.getOrDefault("actions", Lists.newArrayList());
        double maxDistance = (double) map.getOrDefault("maxDistance", 3.0);
        List<EntitySelector> selectors = (List<EntitySelector>) map.getOrDefault("selectors", Lists.newArrayList());
        boolean castFromLocation = (boolean) map.getOrDefault("castFromLocation", true);
        return new AreaEntitiesAction(actions, maxDistance, selectors, castFromLocation);
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
                    forEach(entity -> actions.forEach(action -> action.executeAction(
                            new EntityTarget((LivingEntity) entity),
                            castFromLocation ? new LocationSource(finalL) : source)));
        }
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(LocationTarget.class, EntityTarget.class);
    }
}
