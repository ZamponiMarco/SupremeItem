package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.LocationSource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.entity.Entity;
import com.github.jummes.supremeitem.entity.NoEntity;
import com.github.jummes.supremeitem.projectile.Projectile;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lProjectile", description = "gui.action.projectile.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE1ZmVjNjUxOGE0MWYxNjYxMzFlNjViMTBmNDZmYjg3ZTk3YzQ5MmI0NmRiYzI1ZGUyNjM3NjcyMWZhNjRlMCJ9fX0=")
public class ProjectileAction extends Action {

    private static final String INITIAL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTc2OWUyYzEzNGVlNWZjNmRhZWZlNDEyZTRhZjNkNTdkZjlkYmIzY2FhY2Q4ZTM2ZTU5OTk3OWVjMWFjNCJ9fX0=";
    private static final String GRAVITY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY4ZGZiYzk1YWRiNGY2NDhjMzYxNjRhMTVkNjhlZjVmOWM3Njk3ZDg2Zjg3MjEzYzdkN2E2NDU1NzdhYTY2In19fQ==";
    private static final String ENTITY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTNjOGFhM2ZkZTI5NWZhOWY5YzI3ZjczNGJkYmFiMTFkMzNhMmU0M2U4NTVhY2NkNzQ2NTM1MjM3NzQxM2IifX19";
    private static final String ENTITY_HIT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWVlMTE4ZWRkYWVlMGRmYjJjYmMyYzNkNTljMTNhNDFhN2Q2OGNjZTk0NWU0MjE2N2FhMWRjYjhkMDY3MDUxNyJ9fX0=";
    private static final String BLOCK_HIT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODhiNzY3YzhhMWVhOGU0MDRiM2NjYTg1MzQ5ZjY1M2I1N2IwYzNmNDY0MjdmYmVjZWFjY2YzNjAyYmMyOWUifX19";
    private static final String TICK_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2FhMjVhZTFiOGU2ZTgxY2FkMTU3NTdjMzk3YmYwYzk5M2E1ZDg3NmQ5N2NiZWFlNGEyMGYyNDMzYzMyM2EifX19";
    private static final String HIT_BOX_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjMzYzBiYjM3ZWJlMTE5M2VlNDYxODEwMzQ2MGE3ZjEyOTI3N2E4YzdmZDA4MWI2YWVkYjM0YTkyYmQ1In19fQ==";

    @Serializable(headTexture = INITIAL_HEAD, description = "gui.action.projectile.initial-speed")
    @Serializable.Number(minValue = 0)
    private double initialSpeed;
    @Serializable(headTexture = GRAVITY_HEAD, description = "gui.action.projectile.gravity")
    @Serializable.Number(minValue = 0)
    private double gravity;
    @Serializable(headTexture = ENTITY_HIT_HEAD, description = "gui.action.projectile.entity-hit-actions")
    private List<Action> onEntityHitActions;
    @Serializable(headTexture = BLOCK_HIT_HEAD, description = "gui.action.projectile.block-hit-actions")
    private List<Action> onBlockHitActions;
    @Serializable(headTexture = TICK_HEAD, description = "gui.action.projectile.projectile-tick-actions")
    private List<Action> onProjectileTickActions;
    @Serializable(headTexture = ENTITY_HEAD, description = "gui.action.projectile.entity", recreateTooltip = true)
    private Entity entity;
    @Serializable(headTexture = HIT_BOX_HEAD, description = "gui.action.projectile.hit-box")
    @Serializable.Number(minValue = 0)
    private double hitBoxSize;

    public ProjectileAction() {
        this(10.0, 0.1, Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(), new NoEntity(), 0.5);
    }

    public static ProjectileAction deserialize(Map<String, Object> map) {
        double initialSpeed = (double) map.getOrDefault("initialSpeed", 10.0);
        double gravity = (double) map.getOrDefault("gravity", 0.1);
        List<Action> onEntityHitActions = (List<Action>) map.getOrDefault("onEntityHitActions", Lists.newArrayList());
        List<Action> onBlockHitActions = (List<Action>) map.getOrDefault("onBlockHitActions", Lists.newArrayList());
        List<Action> onProjectileTickActions = (List<Action>) map.getOrDefault("onProjectileTickActions", Lists.newArrayList());
        Entity entity = (Entity) map.getOrDefault("entity", new NoEntity());
        double hitBoxSize = (double) map.getOrDefault("hitBoxSize", 0.5);
        return new ProjectileAction(initialSpeed, gravity, onEntityHitActions, onBlockHitActions,
                onProjectileTickActions, entity, hitBoxSize);
    }

    @Override
    protected void execute(Target target, Source source) {
        Location l = null;
        if (source instanceof EntitySource) {
            EntitySource entitySource = (EntitySource) source;
            l = entitySource.getSource().getEyeLocation().clone();
            if (entitySource.getHand().equals(MainHand.RIGHT)) {
                l = getRightSide(l, 0.5);
            } else {
                l = getLeftSide(l, 0.5);
            }
        } else if (source instanceof LocationSource) {
            l = ((LocationSource) source).getSource();
        }
        if (l != null) {
            new Projectile(source, l, gravity, initialSpeed, onEntityHitActions, onBlockHitActions, onProjectileTickActions,
                    this.entity, this.hitBoxSize);
        }
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE1ZmVjNjUxOGE0MWYxNjYxMzFlNjViMTBmNDZmYjg3ZTk3YzQ5MmI0NmRiYzI1ZGUyNjM3NjcyMWZhNjRlMCJ9fX0="),
                "&6&lProjectile", Libs.getLocale().getList("gui.action.description"));
    }


    public Location getRightSide(Location location, double distance) {
        float angle = location.getYaw() / 60;
        return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }

    public Location getLeftSide(Location location, double distance) {
        float angle = location.getYaw() / 60;
        return location.clone().add(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }
}
