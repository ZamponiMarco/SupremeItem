package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.LocationSource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.entity.Entity;
import com.github.jummes.supremeitem.entity.NoEntity;
import com.github.jummes.supremeitem.projectile.Projectile;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.MainHand;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lProjectile", description = "gui.action.meta.projectile.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmRkNDM2NGZlMmIxOWE2YzExOWQxN2I1Njk0NGVmZjU2NmMxNGI0ZmVhNDVlOWI0YmMzMjkyOGQ1OTdmNDY4In19fQ==")
public class ProjectileAction extends AbstractProjectileAction {

    private static final NumericValue PROJECTILE_SPREAD_DEFAULT = new NumericValue(0);
    private static final boolean SHOOT_FROM_HAND_DEFAULT = false;

    private static final String SHOOT_FROM_HAND_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0=";
    private static final String SPREAD_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTAxMWU3NTE2ZGFhYzVmMjMyMGY2N2I5N2FkNTMwNGY5MDY2Zjg2NDA3YjU4YTUzMGY4MGY4ZmM5N2IzZTg2ZSJ9fX0=";

    @Serializable(headTexture = SHOOT_FROM_HAND_HEAD, description = "gui.action.meta.projectile.shoot-hand")
    @Serializable.Optional(defaultValue = "SHOOT_FROM_HAND_DEFAULT")
    private boolean shootFromHand;

    @Serializable(headTexture = SPREAD_HEAD, description = "gui.action.meta.projectile.spread", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Number(minValue = 0, maxValue = 180, scale = 1)
    @Serializable.Optional(defaultValue = "PROJECTILE_SPREAD_DEFAULT")
    private NumericValue projectileSpread;

    public ProjectileAction() {
        this(TARGET_DEFAULT, INITIAL_DEFAULT.clone(), GRAVITY_DEFAULT.clone(), Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(),
                new NoEntity(), HIT_BOX_SIZE_DEFAULT.clone(), MAX_DISTANCE_DEFAULT.clone(), SHOOT_FROM_HAND_DEFAULT, PROJECTILE_SPREAD_DEFAULT.clone());
    }

    public ProjectileAction(boolean target, NumericValue initialSpeed, NumericValue gravity, List<Action> onEntityHitActions,
                            List<Action> onBlockHitActions, List<Action> onProjectileTickActions, Entity entity,
                            NumericValue hitBoxSize, NumericValue maxDistance, boolean shootFromHand,
                            NumericValue projectileSpread) {
        super(target, initialSpeed, gravity, onEntityHitActions, onBlockHitActions, onProjectileTickActions, entity,
                hitBoxSize, maxDistance);
        this.shootFromHand = shootFromHand;
        this.projectileSpread = projectileSpread;
    }

    public ProjectileAction(Map<String, Object> map) {
        super(map);
        this.shootFromHand = (boolean) map.getOrDefault("shootFromHand", SHOOT_FROM_HAND_DEFAULT);
        this.projectileSpread = (NumericValue) map.getOrDefault("projectileSpread", PROJECTILE_SPREAD_DEFAULT.clone());
    }

    @Override
    public ActionResult execute(Target target, Source source, Map<String, Object> map) {
        Location l = null;
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

        if (l == null) {
            return ActionResult.FAILURE;
        }

        if (!source.getLocation().equals(target.getLocation())) {
            if (target instanceof LocationTarget) {
                l.setDirection(target.getLocation().clone().toVector().subtract(l.toVector()).normalize());
            } else if (target instanceof EntityTarget) {
                l.setDirection(((EntityTarget) target).getTarget().getEyeLocation().clone().toVector().subtract(l.
                        toVector()).normalize());
            }
        }
        new Projectile(target, source, l, gravity.getRealValue(target, source), initialSpeed.getRealValue(target, source),
                onEntityHitActions, onBlockHitActions, onProjectileTickActions,
                this.entity, this.hitBoxSize.getRealValue(target, source), maxDistance.getRealValue(target, source).intValue(),
                projectileSpread.getRealValue(target, source).intValue()).run();
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new ProjectileAction(TARGET_DEFAULT, initialSpeed.clone(), gravity.clone(),
                onEntityHitActions.stream().map(Action::clone).collect(Collectors.toList()),
                onBlockHitActions.stream().map(Action::clone).collect(Collectors.toList()),
                onProjectileTickActions.stream().map(Action::clone).collect(Collectors.toList()),
                entity.clone(), hitBoxSize.clone(), maxDistance.clone(), shootFromHand,
                projectileSpread.clone());
    }

    @Override
    public String getName() {
        return "&6&lProjectile";
    }

    @Override
    public void changeSkillName(String oldName, String newName) {
        onEntityHitActions.forEach(action -> action.changeSkillName(oldName, newName));
        onBlockHitActions.forEach(action -> action.changeSkillName(oldName, newName));
        onProjectileTickActions.forEach(action -> action.changeSkillName(oldName, newName));
    }
}
