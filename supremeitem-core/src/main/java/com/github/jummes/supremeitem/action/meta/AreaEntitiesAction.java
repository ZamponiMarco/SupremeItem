package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.LocationSource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.entity.selector.EntitySelector;
import com.github.jummes.supremeitem.entity.selector.SourceSelector;
import com.github.jummes.supremeitem.placeholder.numeric.NumericValue;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@AllArgsConstructor
@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lApply actions to entities in Area", description = "gui.action.area-entities.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZjZDQxNGIwNWE1MzJjNjA5YzJhYTQ4ZDZjMDYyYzI5MmQ1MzNkZmFmNGQ3MzJhYmU5YWY1NzQxNTg5ZSJ9fX0=")
public class AreaEntitiesAction extends MetaAction {

    private static final double MAX_DISTANCE_DEFAULT = 3.0;
    private static final boolean CAST_LOCATION_DEFAULT = true;

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    private static final String MAX_DISTANCE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    private static final String SELECTOR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM4ZDI3NTk1NjlkNTE1ZDI0NTRkNGE3ODkxYTk0Y2M2M2RkZmU3MmQwM2JmZGY3NmYxZDQyNzdkNTkwIn19fQ==";
    private static final String CAST_LOCATION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQzYjJlM2U5OTU0ZjgyMmI0M2ZlNWY5MTUwOTllMGE2Y2FhYTgxZjc5MTIyMmI1ODAzZDQxNDVhODUxNzAifX19";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.area-entities.actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> actions;

    @Serializable(headTexture = MAX_DISTANCE_HEAD, description = "gui.action.area-entities.max-distance")
    @Serializable.Number(minValue = 0, scale = 1)
    private NumericValue maxDistance;

    @Serializable(headTexture = SELECTOR_HEAD, description = "gui.action.area-entities.selectors")
    private List<EntitySelector> selectors;

    @Serializable(headTexture = CAST_LOCATION_HEAD, description = "gui.action.area-entities.cast-from-location")
    @Serializable.Optional(defaultValue = "CAST_LOCATION_DEFAULT")
    private boolean castFromLocation;

    public AreaEntitiesAction() {
        this(Lists.newArrayList(), new NumericValue(MAX_DISTANCE_DEFAULT), Lists.newArrayList(new SourceSelector()),
                CAST_LOCATION_DEFAULT);
    }

    public static AreaEntitiesAction deserialize(Map<String, Object> map) {
        List<Action> actions = (List<Action>) map.getOrDefault("actions", Lists.newArrayList());
        List<EntitySelector> selectors = (List<EntitySelector>) map.getOrDefault("selectors", Lists.newArrayList());
        boolean castFromLocation = (boolean) map.getOrDefault("castFromLocation", CAST_LOCATION_DEFAULT);
        NumericValue maxDistance;
        try {
            maxDistance = (NumericValue) map.getOrDefault("maxDistance", new NumericValue(MAX_DISTANCE_DEFAULT));
        } catch (ClassCastException e) {
            maxDistance = new NumericValue(((Number) map.getOrDefault("maxDistance", MAX_DISTANCE_DEFAULT)).doubleValue());
        }
        return new AreaEntitiesAction(actions, maxDistance, selectors, castFromLocation);
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        Location l = null;
        if (target instanceof LocationTarget) {
            l = ((LocationTarget) target).getTarget();
        } else if (target instanceof EntityTarget) {
            l = ((EntityTarget) target).getTarget().getLocation();
        }
        if (l != null) {
            double maxDistance = this.maxDistance.getRealValue(target, source);
            Predicate<LivingEntity> select = selectors.stream().map(selector -> selector.getFilter(source)).
                    reduce(e -> true, Predicate::and);
            Location finalL = l;
            World world = l.getWorld();
            if (world != null) {
                l.getWorld().getNearbyEntities(l, maxDistance, maxDistance, maxDistance).stream().
                        filter(entity -> entity instanceof LivingEntity && select.test((LivingEntity) entity)).
                        forEach(entity -> actions.forEach(action -> action.executeAction(
                                new EntityTarget((LivingEntity) entity),
                                castFromLocation ? new LocationSource(finalL) : source)));
            } else {
                return ActionResult.FAILURE;
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(LocationTarget.class, EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZjZDQxNGIwNWE1MzJjNjA5YzJhYTQ4ZDZjMDYyYzI5MmQ1MzNkZmFmNGQ3MzJhYmU5YWY1NzQxNTg5ZSJ9fX0="),
                "&6&lMax radius: &c" + maxDistance, Libs.getLocale().getList("gui.action.description"));
    }
}
