package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.LocationSource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.entity.Entity;
import com.github.jummes.supremeitem.entity.NoEntity;
import com.github.jummes.supremeitem.projectile.HomingProjectile;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.MainHand;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lHoming Projectile", description = "gui.action.projectile.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE1ZmVjNjUxOGE0MWYxNjYxMzFlNjViMTBmNDZmYjg3ZTk3YzQ5MmI0NmRiYzI1ZGUyNjM3NjcyMWZhNjRlMCJ9fX0=")
public class HomingProjectileAction extends AbstractProjectileAction {

    private static final NumericValue PROJECTILE_SPREAD_DEFAULT = new NumericValue(0);
    private static final NumericValue TURN_SPEED_DEFAULT = new NumericValue(0.1);
    private static final boolean SHOOT_FROM_HAND_DEFAULT = false;

    private static final String SHOOT_FROM_HAND_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0=";
    private static final String SPREAD_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTAxMWU3NTE2ZGFhYzVmMjMyMGY2N2I5N2FkNTMwNGY5MDY2Zjg2NDA3YjU4YTUzMGY4MGY4ZmM5N2IzZTg2ZSJ9fX0=";
    private static final String TURN_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjNhYzUxNDc3YThkNDI3MTRjMjU2OTI1ZjQzNmYyYzRkNThhOTUyOWNlOGMxNmExZTM3ZjI2NWU1YzQ4OWEifX19";

    @Serializable(headTexture = SHOOT_FROM_HAND_HEAD, description = "gui.action.projectile.shoot-hand")
    @Serializable.Optional(defaultValue = "SHOOT_FROM_HAND_DEFAULT")
    private boolean shootFromHand;

    @Serializable(headTexture = SPREAD_HEAD, description = "gui.action.projectile.spread", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0, maxValue = 180, scale = 1)
    @Serializable.Optional(defaultValue = "PROJECTILE_SPREAD_DEFAULT")
    private NumericValue projectileSpread;

    @Serializable(headTexture = TURN_HEAD, description = "gui.action.projectile.turn-speed", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0, maxValue = 1)
    @Serializable.Optional(defaultValue = "TURN_SPEED_DEFAULT")
    private NumericValue turnSpeed;

    public HomingProjectileAction() {
        this(TARGET_DEFAULT, INITIAL_DEFAULT.clone(), GRAVITY_DEFAULT.clone(), Lists.newArrayList(), Lists.newArrayList(),
                Lists.newArrayList(), new NoEntity(), HIT_BOX_SIZE_DEFAULT.clone(), MAX_DISTANCE_DEFAULT.clone(),
                SHOOT_FROM_HAND_DEFAULT, PROJECTILE_SPREAD_DEFAULT.clone(), TURN_SPEED_DEFAULT.clone());
    }

    public HomingProjectileAction(boolean target, NumericValue initialSpeed, NumericValue gravity,
                                  List<Action> onEntityHitActions, List<Action> onBlockHitActions,
                                  List<Action> onProjectileTickActions, Entity entity, NumericValue hitBoxSize,
                                  NumericValue maxDistance, boolean shootFromHand, NumericValue projectileSpread,
                                  NumericValue turnSpeed) {
        super(target, initialSpeed, gravity, onEntityHitActions, onBlockHitActions, onProjectileTickActions, entity,
                hitBoxSize, maxDistance);
        this.shootFromHand = shootFromHand;
        this.projectileSpread = projectileSpread;
        this.turnSpeed = turnSpeed;
    }

    public static HomingProjectileAction deserialize(Map<String, Object> map) {
        List<Action> onEntityHitActions = (List<Action>) map.getOrDefault("onEntityHitActions", Lists.newArrayList());
        onEntityHitActions.removeIf(Objects::isNull);
        List<Action> onBlockHitActions = (List<Action>) map.getOrDefault("onBlockHitActions", Lists.newArrayList());
        onBlockHitActions.removeIf(Objects::isNull);
        List<Action> onProjectileTickActions = (List<Action>) map.getOrDefault("onProjectileTickActions", Lists.newArrayList());
        onProjectileTickActions.removeIf(Objects::isNull);

        Entity entity = (Entity) map.getOrDefault("entity", new NoEntity());
        boolean shootFromHand = (boolean) map.getOrDefault("shootFromHand", SHOOT_FROM_HAND_DEFAULT);
        NumericValue initialSpeed = (NumericValue) map.getOrDefault("initialSpeed", INITIAL_DEFAULT.clone());
        NumericValue gravity = (NumericValue) map.getOrDefault("gravity", GRAVITY_DEFAULT.clone());
        NumericValue hitBoxSize = (NumericValue) map.getOrDefault("hitBoxSize", HIT_BOX_SIZE_DEFAULT.clone());
        NumericValue maxDistance = (NumericValue) map.getOrDefault("maxDistance", MAX_DISTANCE_DEFAULT.clone());
        NumericValue projectileSpread = (NumericValue) map.getOrDefault("projectileSpread", PROJECTILE_SPREAD_DEFAULT.clone());
        NumericValue turnSpeed = (NumericValue) map.getOrDefault("turnSpeed", TURN_SPEED_DEFAULT.clone());
        return new HomingProjectileAction(TARGET_DEFAULT, initialSpeed, gravity, onEntityHitActions, onBlockHitActions,
                onProjectileTickActions, entity, hitBoxSize, maxDistance, shootFromHand, projectileSpread, turnSpeed);
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        Location l = null;

        if (!(target instanceof EntityTarget)) {
            return ActionResult.FAILURE;
        }

        if (source.getCaster().equals(((EntityTarget) target).getTarget())) {
            return ActionResult.FAILURE;
        }

        if (source instanceof EntitySource) {
            EntitySource entitySource = (EntitySource) source;
            l = entitySource.getCaster().getEyeLocation().clone();
            if (shootFromHand) {
                if (entitySource.getHand().equals(MainHand.RIGHT)) {
                    l = getRightSide(l, 0.5);
                } else {
                    l = getLeftSide(l, 0.5);
                }
            }
        } else if (source instanceof LocationSource) {
            l = source.getLocation();
        }

        if (l != null) {
            LivingEntity entity = ((EntityTarget) target).getTarget();
            l.setDirection(entity.getEyeLocation().clone().toVector().subtract(l.toVector()).normalize());
            new HomingProjectile(target, source, l, gravity.getRealValue(target, source), initialSpeed.getRealValue(target, source),
                    onEntityHitActions, onBlockHitActions, onProjectileTickActions,
                    this.entity, this.hitBoxSize.getRealValue(target, source), maxDistance.getRealValue(target, source).intValue(),
                    projectileSpread.getRealValue(target, source).intValue(), entity, turnSpeed.getRealValue(target, source)).
                    run();
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new HomingProjectileAction(TARGET_DEFAULT, initialSpeed.clone(), gravity.clone(),
                onEntityHitActions.stream().map(Action::clone).collect(Collectors.toList()),
                onBlockHitActions.stream().map(Action::clone).collect(Collectors.toList()),
                onProjectileTickActions.stream().map(Action::clone).collect(Collectors.toList()),
                entity.clone(), hitBoxSize.clone(), maxDistance.clone(), shootFromHand,
                projectileSpread.clone(), turnSpeed.clone());
    }

    @Override
    public String getName() {
        return "&6&lHoming Projectile";
    }

    @Override
    public Set<SavedSkill> getUsedSavedSkills() {
        Set<SavedSkill> skills = new HashSet<>();
        SavedSkill.addSkillsFromActionsList(skills, onProjectileTickActions);
        SavedSkill.addSkillsFromActionsList(skills, onEntityHitActions);
        SavedSkill.addSkillsFromActionsList(skills, onBlockHitActions);
        return skills;
    }
}
