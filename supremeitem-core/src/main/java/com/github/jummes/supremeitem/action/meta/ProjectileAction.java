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
import com.github.jummes.supremeitem.projectile.Projectile;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@Enumerable.Child
public class ProjectileAction extends Action {

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = HEAD)
    private double initialSpeed;
    @Serializable(headTexture = HEAD)
    private double gravity;
    @Serializable(headTexture = HEAD)
    private List<Action> onEntityHitActions;
    @Serializable(headTexture = HEAD)
    private List<Action> onBlockHitActions;
    @Serializable(headTexture = HEAD)
    private List<Action> onProjectileTickActions;
    @Serializable(headTexture = HEAD)
    private Entity entity;

    public ProjectileAction() {
        this(10.0, 0.1, Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(), new NoEntity());
    }

    public static ProjectileAction deserialize(Map<String, Object> map) {
        double initialSpeed = (double) map.getOrDefault("initialSpeed", 10.0);
        double gravity = (double) map.getOrDefault("gravity", 0.1);
        List<Action> onEntityHitActions = (List<Action>) map.getOrDefault("onEntityHitActions", Lists.newArrayList());
        List<Action> onBlockHitActions = (List<Action>) map.getOrDefault("onBlockHitActions", Lists.newArrayList());
        List<Action> onProjectileTickActions = (List<Action>) map.getOrDefault("onProjectileTickActions", Lists.newArrayList());
        Entity entity = (Entity) map.getOrDefault("entity", new NoEntity());
        return new ProjectileAction(initialSpeed, gravity, onEntityHitActions, onBlockHitActions, onProjectileTickActions, entity);
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
                    this.entity);
        }
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return new ItemStack(Material.ARROW);
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
