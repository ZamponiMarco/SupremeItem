package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.LocationSource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.entity.selector.EntitySelector;
import com.github.jummes.supremeitem.entity.selector.SourceSelector;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lApply actions to entities in Area", description = "gui.action.area-entities.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZjZDQxNGIwNWE1MzJjNjA5YzJhYTQ4ZDZjMDYyYzI5MmQ1MzNkZmFmNGQ3MzJhYmU5YWY1NzQxNTg5ZSJ9fX0=")
public class AreaEntitiesAction extends MetaAction {

    private static final NumericValue MAX_DISTANCE_DEFAULT = new NumericValue(3.0);
    private static final boolean CAST_LOCATION_DEFAULT = true;

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    private static final String MAX_DISTANCE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ2YjEyOTNkYjcyOWQwMTBmNTM0Y2UxMzYxYmJjNTVhZTVhOGM4ZjgzYTE5NDdhZmU3YTg2NzMyZWZjMiJ9fX0=";
    private static final String SELECTOR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM4ZDI3NTk1NjlkNTE1ZDI0NTRkNGE3ODkxYTk0Y2M2M2RkZmU3MmQwM2JmZGY3NmYxZDQyNzdkNTkwIn19fQ==";
    private static final String CAST_LOCATION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQzYjJlM2U5OTU0ZjgyMmI0M2ZlNWY5MTUwOTllMGE2Y2FhYTgxZjc5MTIyMmI1ODAzZDQxNDVhODUxNzAifX19";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.area-entities.actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> actions;

    @Serializable(headTexture = MAX_DISTANCE_HEAD, description = "gui.action.area-entities.max-distance", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0, scale = 1)
    @Serializable.Optional(defaultValue = "MAX_DISTANCE_DEFAULT")
    private NumericValue maxDistance;

    @Serializable(headTexture = SELECTOR_HEAD, description = "gui.action.area-entities.selectors")
    private List<EntitySelector> selectors;

    @Serializable(headTexture = CAST_LOCATION_HEAD, description = "gui.action.area-entities.cast-from-location")
    @Serializable.Optional(defaultValue = "CAST_LOCATION_DEFAULT")
    private boolean castFromLocation;

    public AreaEntitiesAction() {
        this(TARGET_DEFAULT, Lists.newArrayList(), MAX_DISTANCE_DEFAULT.clone(), Lists.newArrayList(new SourceSelector()),
                CAST_LOCATION_DEFAULT);
    }

    public AreaEntitiesAction(boolean target, List<Action> actions, NumericValue maxDistance, List<EntitySelector> selectors, boolean castFromLocation) {
        super(target);
        this.actions = actions;
        this.maxDistance = maxDistance;
        this.selectors = selectors;
        this.castFromLocation = castFromLocation;
    }

    public static AreaEntitiesAction deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        List<Action> actions = (List<Action>) map.getOrDefault("actions", Lists.newArrayList());
        List<EntitySelector> selectors = (List<EntitySelector>) map.getOrDefault("selectors", Lists.newArrayList());
        boolean castFromLocation = (boolean) map.getOrDefault("castFromLocation", CAST_LOCATION_DEFAULT);
        NumericValue maxDistance;
        try {
            maxDistance = (NumericValue) map.getOrDefault("maxDistance", MAX_DISTANCE_DEFAULT.clone());
        } catch (ClassCastException e) {
            maxDistance = new NumericValue(((Number) map.getOrDefault("maxDistance", MAX_DISTANCE_DEFAULT.getValue())));
        }
        return new AreaEntitiesAction(target, actions, maxDistance, selectors, castFromLocation);
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        Location l = target.getLocation();

        LivingEntity caster = source.getCaster();
        if (l != null) {
            double maxDistance = this.maxDistance.getRealValue(target, source);
            Predicate<LivingEntity> select = selectors.stream().map(selector -> selector.getFilter(source)).
                    reduce(e -> true, Predicate::and);
            Location finalL = l;
            World world = l.getWorld();
            if (world != null) {
                l.getWorld().getNearbyEntities(l, maxDistance, maxDistance, maxDistance).stream().
                        filter(entity -> entity instanceof LivingEntity && select.test((LivingEntity) entity)).
                        forEach(entity -> actions.forEach(action -> action.execute(
                                new EntityTarget((LivingEntity) entity),
                                castFromLocation ? new LocationSource(finalL, caster) : source)));
            } else {
                return ActionResult.FAILURE;
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new AreaEntitiesAction(target, actions.stream().map(Action::clone).collect(Collectors.toList()), maxDistance.clone(),
                selectors.stream().map(EntitySelector::clone).collect(Collectors.toList()), castFromLocation);
    }

    @Override
    public String getName() {
        return "&6&lMax radius: &c" + maxDistance.getName();
    }
}
