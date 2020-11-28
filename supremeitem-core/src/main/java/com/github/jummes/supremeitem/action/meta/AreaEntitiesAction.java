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
import com.github.jummes.supremeitem.entity.sorter.EntitySorter;
import com.github.jummes.supremeitem.entity.sorter.ProximitySorter;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lApply actions to entities in Area", description = "gui.action.area-entities.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZjZDQxNGIwNWE1MzJjNjA5YzJhYTQ4ZDZjMDYyYzI5MmQ1MzNkZmFmNGQ3MzJhYmU5YWY1NzQxNTg5ZSJ9fX0=")
public class AreaEntitiesAction extends MetaAction {

    private static final NumericValue MAX_DISTANCE_DEFAULT = new NumericValue(3.0);
    private static final boolean CAST_LOCATION_DEFAULT = true;
    private static final NumericValue COUNT_DEFAULT = new NumericValue(0);
    private static final EntitySorter SORTER_DEFAULT = new ProximitySorter();

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    private static final String MAX_DISTANCE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ2YjEyOTNkYjcyOWQwMTBmNTM0Y2UxMzYxYmJjNTVhZTVhOGM4ZjgzYTE5NDdhZmU3YTg2NzMyZWZjMiJ9fX0=";
    private static final String SELECTOR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM4ZDI3NTk1NjlkNTE1ZDI0NTRkNGE3ODkxYTk0Y2M2M2RkZmU3MmQwM2JmZGY3NmYxZDQyNzdkNTkwIn19fQ==";
    private static final String CAST_LOCATION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQzYjJlM2U5OTU0ZjgyMmI0M2ZlNWY5MTUwOTllMGE2Y2FhYTgxZjc5MTIyMmI1ODAzZDQxNDVhODUxNzAifX19";
    private static final String SORTER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmE2ZDUxYzIyYzg5NTgyODVjMDBhYWFmOTNiNjIxYzE1YmUxMGFhMDQ4MzhhZmUxZDg5Y2Q5YzM2MDMxNDRkZiJ9fX0=";
    private static final String COUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.area-entities.actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> actions;

    @Serializable(headTexture = MAX_DISTANCE_HEAD, description = "gui.action.area-entities.max-distance",
            additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0)
    @Serializable.Optional(defaultValue = "MAX_DISTANCE_DEFAULT")
    private NumericValue maxDistance;

    @Serializable(headTexture = SELECTOR_HEAD, description = "gui.action.area-entities.selectors")
    private List<EntitySelector> selectors;


    @Serializable(headTexture = COUNT_HEAD, description = "gui.action.area-entities.count")
    @Serializable.Number(minValue = 0, scale = 1)
    private NumericValue count;

    @Serializable(headTexture = SORTER_HEAD, description = "gui.action.area-entities.sorter",
            additionalDescription = {"gui.additional-tooltips.recreate"})
    private EntitySorter sorter;

    @Serializable(headTexture = CAST_LOCATION_HEAD, description = "gui.action.area-entities.cast-from-location")
    @Serializable.Optional(defaultValue = "CAST_LOCATION_DEFAULT")
    private boolean castFromLocation;

    public AreaEntitiesAction() {
        this(TARGET_DEFAULT, Lists.newArrayList(), MAX_DISTANCE_DEFAULT.clone(), Lists.
                        newArrayList(new SourceSelector(true)), CAST_LOCATION_DEFAULT, SORTER_DEFAULT.clone(),
                COUNT_DEFAULT.clone());
    }

    public AreaEntitiesAction(boolean target, List<Action> actions, NumericValue maxDistance,
                              List<EntitySelector> selectors, boolean castFromLocation, EntitySorter sorter, NumericValue count) {
        super(target);
        this.actions = actions;
        this.maxDistance = maxDistance;
        this.selectors = selectors;
        this.castFromLocation = castFromLocation;
        this.sorter = sorter;
        this.count = count;
    }

    public static AreaEntitiesAction deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        List<Action> actions = (List<Action>) map.getOrDefault("actions", Lists.newArrayList());
        List<EntitySelector> selectors = (List<EntitySelector>) map.getOrDefault("selectors", Lists.newArrayList());
        boolean castFromLocation = (boolean) map.getOrDefault("castFromLocation", CAST_LOCATION_DEFAULT);
        NumericValue maxDistance = (NumericValue) map.getOrDefault("maxDistance", MAX_DISTANCE_DEFAULT.clone());
        EntitySorter sorter = (EntitySorter) map.getOrDefault("sorter", SORTER_DEFAULT.clone());
        NumericValue count = (NumericValue) map.getOrDefault("count", COUNT_DEFAULT.clone());
        return new AreaEntitiesAction(target, actions, maxDistance, selectors, castFromLocation, sorter, count);
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        Location l = target.getLocation();

        LivingEntity caster = source.getCaster();
        if (l != null) {
            double maxDistance = this.maxDistance.getRealValue(target, source);
            Predicate<LivingEntity> select = selectors.stream().map(selector -> selector.getFilter(source, target)).
                    reduce(e -> true, Predicate::and);
            World world = l.getWorld();
            if (world != null) {
                List<Entity> entities = new ArrayList<>(l.getWorld().getNearbyEntities(l, maxDistance, maxDistance,
                        maxDistance, entity -> entity instanceof LivingEntity && select.test((LivingEntity) entity)));
                Stream<Entity> entityStream = entities.stream();
                int realCount = count.getRealValue(target, source).intValue();
                if (realCount > 0) {
                    sorter.sortCollection(entities, target, source);
                    entityStream = entityStream.limit(realCount);
                }
                entityStream.map(entity -> (LivingEntity) entity).
                        forEach(entity -> actions.forEach(action -> action.execute(new EntityTarget(entity),
                                castFromLocation ? new LocationSource(l, caster) : source)));
            } else {
                return ActionResult.FAILURE;
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new AreaEntitiesAction(target, actions.stream().map(Action::clone).collect(Collectors.toList()), maxDistance.clone(),
                selectors.stream().map(EntitySelector::clone).collect(Collectors.toList()), castFromLocation, sorter.clone(),
                count.clone());
    }

    @Override
    public String getName() {
        return "&6&lMax radius: &c" + maxDistance.getName();
    }

    @Override
    public Set<SavedSkill> getUsedSavedSkills() {
        Set<SavedSkill> skills = new HashSet<>();
        SavedSkill.addSkillsFromActionsList(skills, actions);
        return skills;
    }
}
